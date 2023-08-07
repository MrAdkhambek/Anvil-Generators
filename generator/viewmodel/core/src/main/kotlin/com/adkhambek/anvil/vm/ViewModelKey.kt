package com.adkhambek.anvil.vm

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.reflect.KClass

@MapKey
@MustBeDocumented
@Target(FUNCTION)
@Retention(RUNTIME)
public annotation class ViewModelKey(val value: KClass<out ViewModel>)
