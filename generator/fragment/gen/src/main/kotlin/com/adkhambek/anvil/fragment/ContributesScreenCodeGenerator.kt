package com.adkhambek.anvil.fragment

import androidx.fragment.app.Fragment
import com.adkhambek.anvil.screen.ContributesScreen
import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.api.createGeneratedFile
import com.squareup.anvil.compiler.internal.buildFile
import com.squareup.anvil.compiler.internal.fqName
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.asClassName
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import java.io.File

@AutoService(CodeGenerator::class)
public class ContributesScreenCodeGenerator : CodeGenerator {

    private companion object {
        private val contributesScreenFqName = ContributesScreen::class.fqName
        private val fragmentClasName = Fragment::class.asClassName()
        private val screenKeyClasName = ScreenKey::class.asClassName()
    }

    override fun isApplicable(context: AnvilContext): Boolean = true

    override fun generateCode(
        codeGenDir: File,
        module: ModuleDescriptor,
        projectFiles: Collection<KtFile>,
    ): Collection<GeneratedFile> {
        return projectFiles.classAndInnerClassReferences(module)
            .filter { it.isAnnotatedWith(contributesScreenFqName) }
            .map { generateModule(it, codeGenDir) }
            .toList()
    }

    private fun generateModule(fragmentClass: ClassReference, codeGenDir: File): GeneratedFile {
        val generatedPackage = fragmentClass.packageFqName.toString()
        val moduleClassName = "${fragmentClass.shortName}_Module"

        val scope = fragmentClass.annotations
            .single { it.fqName == contributesScreenFqName }
            .scope()
            .asClassName()

        // Generate a Dagger module file called MyApi_Module.
        val content = FileSpec.buildFile(generatedPackage, moduleClassName) {
            addType(
                TypeSpec.interfaceBuilder(moduleClassName)
                    .addAnnotation(Module::class)
                    .addAnnotation(AnnotationSpec.builder(ContributesTo::class).addMember("%T::class", scope).build())
                    .addFunction(
                        // @Binds
                        // @IntoMap()
                        // bindMyFragment(binder: MyFragment): Fragment
                        FunSpec.builder("bind${fragmentClass.shortName}")
                            .addParameter("fragment", fragmentClass.asClassName())
                            .addModifiers(KModifier.ABSTRACT)
                            .returns(fragmentClasName)
                            .addAnnotation(Binds::class)
                            .addAnnotation(IntoMap::class)
                            .addAnnotation(
                                AnnotationSpec.builder(screenKeyClasName).addMember(
                                    "%T::class",
                                    fragmentClass.asClassName()
                                ).build(),
                            )
                            .build(),
                    )
                    .build(),
            )
        }

        return createGeneratedFile(codeGenDir, generatedPackage, moduleClassName, content)
    }
}
