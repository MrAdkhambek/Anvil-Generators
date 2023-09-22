package com.adkhambek.kunkka.fragment

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

/**
 * Dagger MapKey wrapper for fragment
 *
 * ```
 * @ScreenKey(YourFragment::class)
 * fun bindYourFragment(fragment: YourFragment) : Fragment
 * ```
 */
@MapKey
@MustBeDocumented
@Target(FUNCTION)
@Retention(RUNTIME)
public annotation class ScreenKey(val value: KClass<out Fragment>)
