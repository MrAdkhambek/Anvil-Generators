package com.adkhambek.anvil.fragment

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

@MapKey
@MustBeDocumented
@Target(FUNCTION)
@Retention(RUNTIME)
public annotation class ScreenKey(val value: KClass<out Fragment>)
