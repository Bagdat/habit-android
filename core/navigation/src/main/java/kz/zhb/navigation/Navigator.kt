package kz.zhb.navigation

import androidx.navigation3.runtime.NavKey

interface Navigator {
    fun navigate(key: NavKey)

    /**
     * Вернуться к экрану [key], убрав всё, что над ним: A → B → C → D, pop(B) → A → B.
     * Если [key] в стеке нет — ничего не происходит.
     */
    fun pop(key: NavKey)

    fun back()

    /** Очистить стек и открыть экран (например, splash → onboarding без возврата назад). */
    fun replaceAll(key: NavKey)
}

internal class BackStackNavigator(private val backStack: MutableList<NavKey>) : Navigator {

    override fun navigate(key: NavKey) {
        backStack.add(key)
    }

    override fun pop(key: NavKey) {
        // lastIndexOf: если ключ встречается несколько раз, возвращаемся к ближайшему
        val index = backStack.lastIndexOf(key)
        if (index == -1) return
        backStack.subList(index + 1, backStack.size).clear()
    }

    override fun back() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    override fun replaceAll(key: NavKey) {
        backStack.add(key)
        backStack.subList(0, backStack.lastIndex).clear()
    }
}
