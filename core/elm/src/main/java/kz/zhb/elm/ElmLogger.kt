package kz.zhb.elm

import android.util.Log

/**
 * Логгер в стиле Elmslie: ElmViewModel сообщает о каждом шаге одной строкой —
 * "New event", "New effect", "Executing command", "Command … produces event …".
 */
interface ElmLogger {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String, error: Throwable)
}

/** Глобальная настройка: задаётся один раз в Application. null — логирование выключено. */
object Elm {
    var logger: ElmLogger? = null
}

/** Пишет в Logcat с тегом "Elm/<ViewModel>"; длинные сообщения обрезаются до [maxLength]. */
class LogcatElmLogger(private val maxLength: Int = 1000) : ElmLogger {

    override fun debug(tag: String, message: String) {
        Log.e("Elm/$tag", message.short())
    }

    override fun error(tag: String, message: String, error: Throwable) {
        Log.e("Elm/$tag", message.short(), error)
    }

    private fun String.short(): String =
        if (length <= maxLength) this else take(maxLength) + "… ($length симв.)"
}
