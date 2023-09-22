package com.adkhambek.anvil.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

public interface AssistedSavedStateViewModelFactory<T : ViewModel> {
    public fun create(savedStateHandle: SavedStateHandle): T
}
