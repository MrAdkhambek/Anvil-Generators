package com.adkhambek.anvil.screen

import kotlin.reflect.KClass

/**
 * ```
 * @ContributesScreen(YourCustomScope::class)
 * class YourFragment @Inject constructor(): Fragment { .. }
 * ```
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
public annotation class ContributesScreen(val scope: KClass<*>)
