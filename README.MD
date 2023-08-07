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
    private val assistedFactories: DaggerMap<Class<out ViewModel>, AssistedSavedStateViewModelFactory<out ViewModel>>,
    private val viewModelProviders: DaggerMap<Class<out ViewModel>, Provider<ViewModel>>
) : InjectingSavedStateViewModelFactory {
    
    override fun create(owner: SavedStateRegistryOwner, defaultArgs: Bundle?) =
        object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val viewModel = createAssistedInjectViewModel(modelClass, handle)
                    ?: createInjectViewModel(modelClass)
                    ?: throw IllegalArgumentException("Unknown model class $modelClass")

                try {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    
    private fun <T : ViewModel?> createAssistedInjectViewModel(
        modelClass: Class<T>,
        handle: SavedStateHandle
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
```