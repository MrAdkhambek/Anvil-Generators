@file:OptIn(ExperimentalCompilerApi::class)

package com.adkhambek.kunkka.api

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.internal.testing.compileAnvil
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.OK
import dagger.Provides
import dagger.Reusable
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Assert
import org.junit.Test
import java.lang.reflect.AnnotatedElement

@OptIn(ExperimentalAnvilApi::class)
internal class GeneratorContributesAPITest {

    @Test
    fun `a dagger module is generated`() {
        compileAnvil(
            """
                package com.test

                import com.adkhambek.kunkka.api.ContributesAPI
                import com.adkhambek.anvil.api.TestScope

                @ContributesAPI(TestScope::class)
                interface TestApi
            """
        ) {
            Assert.assertEquals(exitCode, OK)

            val clazz = classLoader.loadClass("com.test.TestApi_Module")

            val contributesToAnnotation = clazz.annotation<ContributesTo>()
            Assert.assertEquals(contributesToAnnotation.scope, TestScope::class)

            val providerMethod = clazz.declaredMethods.single()

            Assert.assertEquals(providerMethod.returnType, classLoader.loadClass("com.test.TestApi"))
            Assert.assertTrue(
                providerMethod.annotations.map { it.annotationClass }.containsAll(
                    listOf(Provides::class, Reusable::class)
                )
            )

            val param = providerMethod.parameters.single()
            Assert.assertEquals(param.type, classLoader.loadClass("retrofit2.Retrofit"))
        }
    }
}

public inline fun <reified T> AnnotatedElement.annotationOrNull(): T? =
    annotations.singleOrNull { it.annotationClass == T::class } as? T

public inline fun <reified T> AnnotatedElement.annotation(): T =
    requireNotNull(annotationOrNull<T>()) { "Couldn't find annotation ${T::class}" }
