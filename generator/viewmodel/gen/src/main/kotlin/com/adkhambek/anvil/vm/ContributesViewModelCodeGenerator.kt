package com.adkhambek.anvil.vm

import androidx.lifecycle.ViewModel
import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.compiler.api.AnvilCompilationException
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.asClassName
import com.squareup.anvil.compiler.internal.buildFile
import com.squareup.anvil.compiler.internal.fqName
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.asClassName
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import dagger.Binds
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.multibindings.IntoMap
import java.io.File
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile

@AutoService(CodeGenerator::class)
public class ContributesViewModelCodeGenerator : CodeGenerator {

    private companion object {
        private val assistedFqName = Assisted::class.fqName
        private val assistedInjectFqName = AssistedInject::class.fqName
        private val contributesViewModelFqName = ContributesViewModel::class.fqName

        private val viewModelClassName = ViewModel::class.asClassName()
        private val viewModelKeyClassName = ViewModelKey::class.asClassName()
        private val assistedViewModelFactoryClassName = AssistedSavedStateViewModelFactory::class.asClassName()
    }

    override fun isApplicable(context: AnvilContext): Boolean = true

    override fun generateCode(
        codeGenDir: File,
        module: ModuleDescriptor,
        projectFiles: Collection<KtFile>,
    ): Collection<GeneratedFile> {
        return projectFiles
            .classAndInnerClassReferences(module)
            .filter { clazz ->
                clazz.isAnnotatedWith(contributesViewModelFqName)
            }.flatMap { clazz ->
                val scope = clazz.annotations.first { psi ->
                    psi.fqName == contributesViewModelFqName
                }.scope()

                generateBindModule(
                    codeGenDir = codeGenDir,
                    vmClass = clazz,
                    scope = scope,
                    module = module
                )
            }.toList()
    }

    private fun generateBindModule(
        codeGenDir: File,
        scope: ClassReference,
        vmClass: ClassReference.Psi,
        module: ModuleDescriptor,
    ): List<GeneratedFile> {
        val vmClassShortName = vmClass.shortName
        val modulePackage = vmClass.packageFqName.asString()
        val moduleInterfaceName = "${vmClassShortName}Module"

        val generatedFiles: MutableList<GeneratedFile> = mutableListOf()
        val isAssistedInjector = vmClass.constructors.any { it.isAnnotatedWith(assistedInjectFqName) }

        val (returns, binder) = if (isAssistedInjector) {
            val assistedFactoryClassName = "${vmClassShortName}AssistedFactory"

            generatedFiles += generateAssistedFactory(
                vmClass = vmClass,
                codeGenDir = codeGenDir,
                assistedFactoryClassName = assistedFactoryClassName
            )

            val factoryClassName = assistedViewModelFactoryClassName.parameterizedBy(
                WildcardTypeName.producerOf(viewModelClassName)
            )
            factoryClassName to vmClass.packageFqName.child(Name.identifier(assistedFactoryClassName))
                .asClassName(module)
        } else {
            viewModelClassName to vmClass.asClassName()
        }

        val bindFunction = FunSpec.builder("bind${vmClassShortName}Factory")
            .addParameter("viewModel", binder)
            .addModifiers(KModifier.ABSTRACT)
            .addAnnotation(Binds::class)
            .addAnnotation(IntoMap::class)
            .addAnnotation(
                AnnotationSpec
                    .builder(viewModelKeyClassName)
                    .addMember(
                        "%T::class",
                        vmClass.asClassName()
                    ).build(),
            )
            .returns(returns)
            .build()

        val content = FileSpec.buildFile(modulePackage, moduleInterfaceName) {
            addType(
                TypeSpec
                    .interfaceBuilder(
                        ClassName(
                            modulePackage,
                            moduleInterfaceName
                        )
                    )
                    .addAnnotation(Module::class)
                    .addAnnotation(
                        AnnotationSpec
                            .builder(ContributesTo::class)
                            .addMember("%T::class", scope.asClassName())
                            .build(),
                    )
                    .addFunction(bindFunction)
                    .build()
            )
        }

        generatedFiles += createGeneratedFile(codeGenDir, modulePackage, moduleInterfaceName, content)
        return generatedFiles
    }

    private fun generateAssistedFactory(
        vmClass: ClassReference.Psi,
        assistedFactoryClassName: String,
        codeGenDir: File,
    ): GeneratedFile {
        val generatedPackage = vmClass.packageFqName.toString()

        val constructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(assistedInjectFqName) }
        val assistedParameter = constructor?.parameters?.singleOrNull { it.isAnnotatedWith(assistedFqName) }

        if (constructor == null || assistedParameter == null) {
            throw AnvilCompilationException(
                "${vmClass.fqName} must have an @AssistedInject constructor with @Assisted savedStateHandle: SavedStateHandle parameter",
                element = vmClass.clazz.identifyingElement,
            )
        }

        if (assistedParameter.name != "savedStateHandle") {
            throw AnvilCompilationException(
                "${vmClass.fqName} @Assisted parameter must be named savedStateHandle",
                element = assistedParameter.parameter.identifyingElement,
            )
        }

        val vmClassName = vmClass.asClassName()
        val stateClassName = assistedParameter.type().asTypeName()

        val content = FileSpec.buildFile(generatedPackage, assistedFactoryClassName) {
            addType(
                TypeSpec.interfaceBuilder(assistedFactoryClassName)
                    .addSuperinterface(assistedViewModelFactoryClassName.parameterizedBy(vmClassName))
                    .addAnnotation(AssistedFactory::class)
                    .addFunction(
                        FunSpec.builder("create")
                            .addModifiers(KModifier.OVERRIDE, KModifier.ABSTRACT)
                            .addParameter("savedStateHandle", stateClassName)
                            .returns(vmClassName)
                            .build(),
                    )
                    .build(),
            )
        }

        return createGeneratedFile(codeGenDir, generatedPackage, assistedFactoryClassName, content)
    }
}
