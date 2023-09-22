@file:OptIn(ExperimentalCompilerApi::class)

package com.adkhambek.kunkka.fragment

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.compiler.internal.testing.compileAnvil
import com.tschuchort.compiletesting.KotlinCompilation
import dagger.Binds
import dagger.multibindings.IntoMap
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.AnnotatedElement

internal class ContributesScreenCodeGeneratorTest {

    @Test
    fun `a dagger module is generated`() {
        compileAnvil(
            """
                package com.test

                import com.adkhambek.anvil.screen.ContributesScreen
                import androidx.fragment.app.Fragment
                import com.adkhambek.anvil.fragment.TestScope
                import javax.inject.Inject

                @ContributesScreen(TestScope::class)
                class TestFragment @Inject constructor(): Fragment()
            """
        ) {
            assertEquals(KotlinCompilation.ExitCode.OK, exitCode)

            val clazz = classLoader.loadClass("com.test.TestFragment_Module")

            val contributesToAnnotation = clazz.annotation<ContributesTo>()
            assertEquals(contributesToAnnotation.scope, TestScope::class)

            val providerMethod = clazz.declaredMethods.single()
            assertEquals(providerMethod.returnType, classLoader.loadClass("androidx.fragment.app.Fragment"))

            val isCorrectAnnotations = providerMethod
                .annotations
                .map { it.annotationClass }
                .containsAll(
                    listOf(Binds::class, IntoMap::class, ScreenKey::class)
                )

            assertEquals(3, providerMethod.annotations.size)
            assertTrue(isCorrectAnnotations)

            val param = providerMethod.parameters.single()
            assertEquals(param.type, classLoader.loadClass("com.test.TestFragment"))
        }
    }
}

public inline fun <reified T> AnnotatedElement.annotationOrNull(): T? =
    annotations.singleOrNull { it.annotationClass == T::class } as? T

public inline fun <reified T> AnnotatedElement.annotation(): T =
    requireNotNull(annotationOrNull<T>()) { "Couldn't find annotation ${T::class}" }
