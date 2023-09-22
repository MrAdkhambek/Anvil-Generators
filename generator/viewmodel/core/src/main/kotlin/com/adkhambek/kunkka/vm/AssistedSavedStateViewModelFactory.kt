package com.adkhambek.kunkka.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

public interface AssistedSavedStateViewModelFactory<T : ViewModel> {
    public fun create(savedStateHandle: SavedStateHandle): T
}
