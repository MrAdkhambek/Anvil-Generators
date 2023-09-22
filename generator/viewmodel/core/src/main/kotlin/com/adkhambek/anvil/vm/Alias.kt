@file:Suppress("MaxLineLength")

package com.adkhambek.anvil.vm

import androidx.lifecycle.ViewModel
import javax.inject.Provider

public typealias AssistedViewModelFactories = Map<Class<out ViewModel>, AssistedSavedStateViewModelFactory<out ViewModel>>
public typealias ViewModelProviders = Map<Class<out ViewModel>, @JvmWildcard Provider<ViewModel>>
