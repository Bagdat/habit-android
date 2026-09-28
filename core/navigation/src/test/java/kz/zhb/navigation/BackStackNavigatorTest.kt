package kz.zhb.navigation

import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Test

class BackStackNavigatorTest {

    private data object A : NavKey
    private data object B : NavKey
    private data object C : NavKey
    private data object D : NavKey
    private data class Details(val id: Int) : NavKey

    private fun navigatorWith(vararg keys: NavKey): Pair<BackStackNavigator, MutableList<NavKey>> {
        val backStack = mutableListOf(*keys)
        return BackStackNavigator(backStack) to backStack
    }

    @Test
    fun `pop возвращает к ключу и убирает всё над ним`() {
        val (navigator, backStack) = navigatorWith(A, B, C, D)

        navigator.pop(B)

        assertEquals(listOf(A, B), backStack)
    }

    @Test
    fun `pop к верхнему экрану ничего не меняет`() {
        val (navigator, backStack) = navigatorWith(A, B, C, D)

        navigator.pop(D)

        assertEquals(listOf(A, B, C, D), backStack)
    }

    @Test
    fun `pop к первому экрану оставляет только его`() {
        val (navigator, backStack) = navigatorWith(A, B, C, D)

        navigator.pop(A)

        assertEquals(listOf(A), backStack)
    }

    @Test
    fun `pop к ключу которого нет ничего не меняет`() {
        val (navigator, backStack) = navigatorWith(A, B, C)

        navigator.pop(D)

        assertEquals(listOf(A, B, C), backStack)
    }

    @Test
    fun `pop при повторах возвращает к ближайшему`() {
        val (navigator, backStack) = navigatorWith(A, B, C, B, D)

        navigator.pop(B)

        assertEquals(listOf(A, B, C, B), backStack)
    }

    @Test
    fun `pop сравнивает ключи с аргументами по значению`() {
        val (navigator, backStack) = navigatorWith(A, Details(1), Details(2), C)

        navigator.pop(Details(1))

        assertEquals(listOf(A, Details(1)), backStack)
    }
}
