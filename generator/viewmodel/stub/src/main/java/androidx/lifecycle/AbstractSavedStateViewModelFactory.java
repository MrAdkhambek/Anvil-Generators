/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.lifecycle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.savedstate.SavedStateRegistryOwner;

public abstract class AbstractSavedStateViewModelFactory
        extends ViewModelProvider.OnRequeryFactory
        implements ViewModelProvider.Factory {

    public AbstractSavedStateViewModelFactory() {
    }

    public AbstractSavedStateViewModelFactory(@NonNull SavedStateRegistryOwner owner,
                                              @Nullable Bundle defaultArgs) {
        throw new RuntimeException("Stub");
    }

    @NonNull
    @Override
    public final <T extends ViewModel> T create(@NonNull Class<T> modelClass,
                                                @NonNull CreationExtras extras) {
        throw new RuntimeException("Stub");
    }

    @NonNull
    private <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass) {
        throw new RuntimeException("Stub");
    }

    @NonNull
    @Override
    public final <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        throw new RuntimeException("Stub");
    }

    /**
     * Creates a new instance of the given {@code Class}.
     * <p>
     *
     * @param key a key associated with the requested ViewModel
     * @param modelClass a {@code Class} whose instance is requested
     * @param handle a handle to saved state associated with the requested ViewModel
     * @param <T> The type parameter for the ViewModel.
     * @return a newly created ViewModels
     */
    @NonNull
    protected abstract <T extends ViewModel> T create(@NonNull String key,
                                                      @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle);

    /**
     * @hide
     */
    @Override
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void onRequery(@NonNull ViewModel viewModel) {
        throw new RuntimeException("Stub");
    }
}
