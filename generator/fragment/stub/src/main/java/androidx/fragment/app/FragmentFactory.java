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

package androidx.fragment.app;


import androidx.annotation.NonNull;

public class FragmentFactory {
    /**
     * Parse a Fragment Class from the given class name. The resulting Class is kept in a global
     * cache, bypassing the {@link Class#forName(String)} calls when passed the same
     * class name again.
     *
     * @param classLoader The default classloader to use for loading the Class
     * @param className   The class name of the fragment to parse.
     * @return Returns the parsed Fragment Class
     * @throws Fragment.InstantiationException If there is a failure in parsing
     *                                         the given fragment class.  This is a runtime exception; it is not
     *                                         normally expected to happen.
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static Class<? extends Fragment> loadFragmentClass(@NonNull ClassLoader classLoader,
                                                              @NonNull String className) {
        throw new RuntimeException("Stub");
    }

    /**
     * Create a new instance of a Fragment with the given class name. This uses
     * {@link #loadFragmentClass(ClassLoader, String)} and the empty
     * constructor of the resulting Class by default.
     *
     * @param classLoader The default classloader to use for instantiation
     * @param className   The class name of the fragment to instantiate.
     * @return Returns a new fragment instance.
     * @throws Fragment.InstantiationException If there is a failure in instantiating
     *                                         the given fragment class.  This is a runtime exception; it is not
     *                                         normally expected to happen.
     */
    @NonNull
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        throw new RuntimeException("Stub");
    }
}
