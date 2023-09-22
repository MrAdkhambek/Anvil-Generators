package com.adkhambek.kunkka.vm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

public interface InjectingSavedStateViewModelFactory {
    public fun create(
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null,
    ): ViewModelProvider.Factory
}
