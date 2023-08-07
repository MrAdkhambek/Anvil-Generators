package com.adkhambek.anvil.vm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

/**
 * Use this class for instantiating your ViewModels. @Inject it into your Fragment/Activity and create ViewModel with it as a factory.
 *
 * For example in Fragment:
 * ```
 * @Inject
 * lateinit var factory: InjectingSavedStateViewModelFactory
 * lateinit var viewModel : SomeViewModel
 *
 * override fun onCreate(savedInstanceState: Bundle?) {
 *   super.onCreate(savedInstanceState)
 *   viewModel = ViewModelProvider(this, factory)[SomeViewModel::class.java]
 * }
 * ```
 * @param assistedFactories used for ViewModels annotated with `@AssistedInject` and having `AssistedInject.Factory`
 * @param viewModelProviders used for ViewModels annotated with `@Inject` (pure Dagger instantiation)
 *
 * @see AssistedSavedStateViewModelFactory for how to get this class
 */

public interface InjectingSavedStateViewModelFactory {
    public fun create(
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null,
    ): ViewModelProvider.Factory
}
