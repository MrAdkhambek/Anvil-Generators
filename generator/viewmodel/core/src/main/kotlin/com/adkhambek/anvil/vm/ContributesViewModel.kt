package com.adkhambek.anvil.vm

import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
public annotation class ContributesViewModel(val scope: KClass<*>)
