package com.adkhambek.anvil.screen

import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
public annotation class ContributesScreen(val scope: KClass<*>)