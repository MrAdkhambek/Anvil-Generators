package com.adkhambek.kunkka.vm

import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
public annotation class ContributesViewModel(val scope: KClass<*>)
