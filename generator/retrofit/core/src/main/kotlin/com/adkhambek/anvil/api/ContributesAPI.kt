package com.adkhambek.anvil.api

import kotlin.reflect.KClass

/**
 * Annotate a retrofit interface with this to automatically contribute it to the specified scope.
 * Equivalent to the following declaration in an application module:
 *
 * ```
 *     @Provides
 *     @Reusable
 *     public fun provideYourApi(
 *          retrofit: Retrofit,
 *     ): YourApi = retrofit.create(YourApi::class.java)
 *```
 */
@Target(AnnotationTarget.CLASS)
public annotation class ContributesAPI(val scope: KClass<*>)
