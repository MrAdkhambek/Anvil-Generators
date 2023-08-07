@file:OptIn(ExperimentalCompilerApi::class)

package com.adkhambek.anvil.vm

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.internal.testing.compileAnvil
import dagger.Provides
import dagger.Reusable
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.OK
import org.junit.Assert
import java.lang.reflect.AnnotatedElement
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test

@OptIn(ExperimentalAnvilApi::class)
internal class ContributesViewModelCodeGeneratorTest {

    @Test
    fun `a dagger module is generated`() {
        compileAnvil(
            """
                package com.test

                import com.adkhambek.anvil.vm.ContributesViewModel
                import androidx.lifecycle.ViewModel
                import com.adkhambek.anvil.vm.TestScope
                import javax.inject.Inject

                @ContributesViewModel(TestScope::class)
                class TestViewModel @Inject constructor() : ViewModel()
            """
        ) {
            Assert.assertEquals(OK, exitCode)

            val clazz = classLoader.loadClass("com.test.TestViewModelModule")

            val contributesToAnnotation = clazz.annotation<ContributesTo>()
            Assert.assertEquals(contributesToAnnotation.scope, TestScope::class)

            val providerMethod = clazz.declaredMethods.single()

            Assert.assertEquals(providerMethod.returnType, classLoader.loadClass("androidx.lifecycle.ViewModel"))

            val param = providerMethod.parameters.single()
            Assert.assertEquals(param.type, classLoader.loadClass("com.test.TestViewModel"))
        }
    }
}
