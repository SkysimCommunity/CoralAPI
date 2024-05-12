package sh.lumin.coral.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.function.Predicate


object Utils {
    private val formatter: NumberFormat = NumberFormat.getInstance()

    private val usernameRegex = Regex("^[a-zA-Z0-9_]{2,16}\$")

    private val UUIDvalidator: Predicate<String> = Predicate {
        try {
            if(it.length != 36) return@Predicate false
            UUID.fromString(it)
            true
        } catch (_: Exception) {

            false
        }
    }

    fun isValidUsername(username: String): Boolean = usernameRegex.matches(username)

    suspend fun validateRequest(call: ApplicationCall): Boolean {
        val api_key = call.request.header("Authentication")

        if(api_key == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "No API key provided!"))
            return false
        }

        if(!UUIDvalidator.test(api_key)) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid API key specified"))
            return false
        }

        return true
    }

    fun parseRomanNumeral(roman: String): Int {
        val map = mapOf('I' to 1, 'V' to 5, 'X' to 10, 'L' to 50, 'C' to 100, 'D' to 500, 'M' to 1000)
        var result = 0
        var prevValue = 0

        for (i in roman.indices.reversed()) {
            val value = map[roman[i]] ?: throw IllegalArgumentException("Invalid Roman numeral")
            result += if (value < prevValue) -value else value
            prevValue = value
        }
        return result
    }

    fun formatNumber(value: Float): String {
        val arr = arrayOf("", "k", "M", "B", "T", "P", "E")
        var index = 0
        var realValue = value
        while (realValue / 1000.0F >= 1.0F) {
            realValue /= 1000.0F
            index++
        }
        val decimalFormat = DecimalFormat("#.#")
        val formattedValue = if (realValue < 1000000.0F)
            decimalFormat.format(realValue)
        else
            realValue.toInt().toString()
        val finalResult = "$formattedValue${arr[index]}".replace(",", ".")
        return if (value <= 20000.0F && value > 0.0F)
            Math.round(value).toString()
        else
            finalResult
    }

    fun formatNumber(value: Long): String = formatNumber(value.toFloat())

    fun commaify(number: Number): String {
        return formatter.format(number)
    }
}