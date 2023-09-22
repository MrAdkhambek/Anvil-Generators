## Anvil generators

| Libraries  |    Version    |
|:-----------|:-------------:|
| Kotlin     |    1.8.22     |
| Kotlin KSP | 1.8.22-1.0.11 |
| Anvil      |   2.4.7-1-8   |
| Dagger     |    2.46.1     |
| Gradle     |      7.6      |

### API (retrofit2) `@ContributesAPI`
```kotlin
@ContributesAPI(CustomScope::class)
interface API {

    @POST("/path")
    suspend fun apiMethod(
        @Body request: Request
    ): Response
}

// don't need to write bind module
class RepositoryImpl @Inject constructor(
    private val api: API,
)
```
---
### Fragment screen generator usage `@ContributesScreen`
```kotlin
@ContributesScreen(CustomScope::class)
class HomeFragment @Inject constructor() : Fragment(R.layout.fragment_home)
```

### Custom FragmentFactory
```kotlin
class CustomFragmentFactory @Inject constructor(
    private val fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val clazz = loadFragmentClass(classLoader, className)
        return fragmentProviders[clazz]?.get() ?: super.instantiate(classLoader, className)
    }
}

// OR
class CustomFragmentFactory @Inject constructor(
    fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>,
) : FragmentScreenFactory(fragmentProviders)

// OR
class CustomFragmentFactory @Inject constructor(
    fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>,
    legacyFragmentFactory: FragmentFactory,
) : FragmentScreenFactory(fragmentProviders, legacyFragmentFactory)
```
---

### ViewModel generator usage `@ContributesViewModel`
```kotlin
@ContributesViewModel(CustomScope::class)
class AuthViewModel @Inject constructor(
    private val myUseCase: MyUseCase
) : ViewModel()

// or with SavedStateHandle
@ContributesViewModel(AuthScope::class)
class AuthViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val myUseCase: MyUseCase
) : ViewModel()
```

```kotlin
class AuthSavedStateViewModelFactory @Inject constructor(
    assistedFactories: DaggerMap<Class<out ViewModel>, AssistedSavedStateViewModelFactory<out ViewModel>>,
    viewModelProviders: DaggerMap<Class<out ViewModel>, Provider<ViewModel>>,
) : DefaultSavedStateViewModelFactory(
    assistedFactories = assistedFactories,
    viewModelProviders = viewModelProviders,
)
```