package com.adkhambek.anvil.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
public annotation class ContributesAPI(val scope: KClass<*>)
