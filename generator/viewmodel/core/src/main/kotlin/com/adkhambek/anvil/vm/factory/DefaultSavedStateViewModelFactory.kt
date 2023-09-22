package com.adkhambek.anvil.vm.factory

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.adkhambek.anvil.vm.AssistedViewModelFactories
import com.adkhambek.anvil.vm.InjectingSavedStateViewModelFactory
import com.adkhambek.anvil.vm.ViewModelProviders

public open class DefaultSavedStateViewModelFactory constructor(
    private val assistedFactories: AssistedViewModelFactories,
    private val viewModelProviders: ViewModelProviders,
) : InjectingSavedStateViewModelFactory {

    final override fun create(owner: SavedStateRegistryOwner, defaultArgs: Bundle?): ViewModelProvider.Factory =
        object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle,
            ): T {
                val viewModel = createAssistedInjectViewModel(modelClass, handle)
                    ?: createInjectViewModel(modelClass)
                    ?: throw IllegalArgumentException("Unknown model class $modelClass")

                try {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                } catch (e: ClassCastException) {
                    throw RuntimeException(e)
                }
            }
        }

    private fun <T : ViewModel?> createAssistedInjectViewModel(
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): ViewModel? {
        val creator = assistedFactories[modelClass]
            ?: assistedFactories.asSequence().firstOrNull {
                modelClass.isAssignableFrom(it.key)
            }?.value
            ?: return null

        return creator.create(handle)
    }

    private fun <T : ViewModel?> createInjectViewModel(modelClass: Class<T>): ViewModel? {
        val creator = viewModelProviders[modelClass]
            ?: viewModelProviders.asSequence().firstOrNull {
                modelClass.isAssignableFrom(it.key)
            }?.value
            ?: return null

        return creator.get()
    }
}
