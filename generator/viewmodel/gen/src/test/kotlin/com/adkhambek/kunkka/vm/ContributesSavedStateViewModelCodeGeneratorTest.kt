@file:OptIn(ExperimentalCompilerApi::class)

package com.adkhambek.kunkka.vm

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.internal.testing.compileAnvil
import com.tschuchort.compiletesting.KotlinCompilation
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Assert
import org.junit.Test
import java.lang.reflect.AnnotatedElement

@OptIn(ExperimentalAnvilApi::class)
internal class ContributesSavedStateViewModelCodeGeneratorTest {

    @Test
    fun `a dagger module and factory are generated`() {
        compileAnvil(
            """
                package com.test

                import com.adkhambek.anvil.vm.ContributesViewModel
                import androidx.lifecycle.ViewModel
                import androidx.lifecycle.SavedStateHandle
                import com.adkhambek.anvil.vm.TestScope
                import dagger.assisted.Assisted
                import dagger.assisted.AssistedInject

                @ContributesViewModel(TestScope::class)
                class TestViewModel @AssistedInject constructor(
                    @Assisted private val savedStateHandle: SavedStateHandle
                ) : ViewModel()
            """
        ) {
            Assert.assertEquals(KotlinCompilation.ExitCode.OK, exitCode)

            run {
                val clazz = classLoader.loadClass("com.test.TestViewModelModule")

                val contributesToAnnotation = clazz.annotation<ContributesTo>()
                Assert.assertEquals(contributesToAnnotation.scope, TestScope::class)

                val providerMethod = clazz.declaredMethods.single()

                Assert.assertEquals(
                    providerMethod.returnType,
                    classLoader.loadClass("com.adkhambek.anvil.vm.AssistedSavedStateViewModelFactory")
                )

                val param = providerMethod.parameters.single()
                Assert.assertEquals(param.type, classLoader.loadClass("com.test.TestViewModelAssistedFactory"))
            }

            run {
                val clazz = classLoader.loadClass("com.test.TestViewModelAssistedFactory")

                val providerMethod = clazz.declaredMethods.single()

                Assert.assertEquals(
                    providerMethod.returnType,
                    classLoader.loadClass("com.test.TestViewModel")
                )

                val param = providerMethod.parameters.single()
                Assert.assertEquals(param.type, classLoader.loadClass("androidx.lifecycle.SavedStateHandle"))
            }
        }
    }
}

internal inline fun <reified T> AnnotatedElement.annotationOrNull(): T? =
    annotations.singleOrNull { it.annotationClass == T::class } as? T

internal inline fun <reified T> AnnotatedElement.annotation(): T =
    requireNotNull(annotationOrNull<T>()) { "Couldn't find annotation ${T::class}" }
