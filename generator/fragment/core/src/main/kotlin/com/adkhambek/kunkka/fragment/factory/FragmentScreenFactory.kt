package com.adkhambek.kunkka.fragment.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Provider

/**
 * Simple usage
 * ```
 * @SingleIn(YourScope::class)
 * @ContributesBinding(YourScope::class, boundType = FragmentFactory::class)
 * class YourFragmentFactory @Inject constructor(
 *     fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>,
 * ) : FragmentScreenFactory(fragmentProviders)
 *
 * class YourActivity : FragmentActivity() {
 *
 *      val yourFragmentFactory: FragmentFactory = TODO()
 *
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          supportFragmentManager.fragmentFactory = yourFragmentFactory
 *          super.onCreate(savedInstanceState)
 *      }
 *  }
 * ```
 *
 * Or usage combine with legacy fragment factory
 * ```
 * @SingleIn(YourScope::class)
 * @ContributesBinding(YourScope::class, boundType = FragmentFactory::class)
 * class YourFragmentFactory @Inject constructor(
 *     fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>,
 *     legacyFragmentFactory: FragmentFactory
 * ) : FragmentScreenFactory(fragmentProviders, legacyFragmentFactory)
 *
 * class YourActivity : FragmentActivity() {
 *
 *      val yourFragmentFactory: FragmentFactory = TODO()
 *
 *      override fun onCreate(savedInstanceState: Bundle?) {
 *          supportFragmentManager.fragmentFactory = yourFragmentFactory
 *          super.onCreate(savedInstanceState)
 *      }
 *  }
 * ```
 */
public open class FragmentScreenFactory public constructor(
    private val fragmentProviders: Map<Class<out Fragment>, Provider<Fragment>>,
    private val delegate: FragmentFactory,
) : FragmentFactory() {

    public constructor(
        fragmentProviders: Map<Class<out Fragment>, Provider<Fragment>>,
    ) : this(fragmentProviders, FragmentFactory())

    final override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val clazz = loadFragmentClass(classLoader, className)
        return fragmentProviders[clazz]?.get() ?: delegate.instantiate(classLoader, className)
    }
}
