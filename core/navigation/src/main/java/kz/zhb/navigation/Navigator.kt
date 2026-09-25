package kz.zhb.navigation

import androidx.navigation3.runtime.NavKey

/** Всё, что фиче нужно знать о навигации. Реализация — внутри core:navigation. */
interface Navigator {
    fun navigate(key: NavKey)
    fun back()

    /** Очистить стек и открыть экран (например, splash → onboarding без возврата назад). */
    fun replaceAll(key: NavKey)
}

internal class BackStackNavigator(private val backStack: MutableList<NavKey>) : Navigator {
    override fun navigate(key: NavKey) {
        backStack.add(key)
    }

    override fun back() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    override fun replaceAll(key: NavKey) {
        backStack.add(key)
        backStack.subList(0, backStack.lastIndex).clear()
    }
}
