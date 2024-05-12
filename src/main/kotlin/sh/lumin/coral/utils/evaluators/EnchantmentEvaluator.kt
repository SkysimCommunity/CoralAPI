package sh.lumin.coral.utils.evaluators

import sh.lumin.coral.utils.Utils
import sh.lumin.coral.utils.removeFormatting

val ENCHANT_PRICE_MAP = mutableMapOf(
    "ender_slayer:15" to 7_500,
    //
    "legion:1" to 20_000,
    "legion:2" to 40_000,
    "legion:3" to 80_000,
    "legion:4" to 160_000,
    "legion:5" to 320_000,
    //
    "fatal_tempo:1" to 45_000,
    "fatal_tempo:2" to 90_000,
    "fatal_tempo:3" to 180_000,
    "fatal_tempo:4" to 360_000,
    "fatal_tempo:5" to 720_000,
    //
    "smite:15" to 4_000,
    //
    "first_strike:7" to 10_000,
    //
    "turbo_gem:1" to 7_500,
    "turbo_gem:2" to 15_000,
    "turbo_gem:3" to 30_000,
    "turbo_gem:4" to 60_000,
    "turbo_gem:5" to 120_000,
    //
    "vicious:10" to 10_000,
    "vicious:15" to 30_000,
    //
    "fatal_tempo:1" to 40_000,
    "luck:8" to 90_000,

)

object EnchantmentEvaluator {
    fun valueEnchants(enchants: List<String>): Int {
        return parseEnchants(enchants).sumOf { valueEnchant(it) }
    }

    private fun valueEnchant(enchant: String): Int {
        return ENCHANT_PRICE_MAP[enchant] ?: 0
    }

    private fun parseEnchants(lines: List<String>): List<String> {
        val enchantMap = mutableListOf<String>()
        var enchantStart = false

        for (line in lines) {
            if (line.isBlank()) {
                enchantStart = !enchantStart
                if (!enchantStart) break
            }
            if (enchantStart && (line.startsWith("§9") || line.startsWith("§d§l"))) {
                if (line.contains(", §9")) {
                    line.split(", §9").map { it.removeFormatting() }.forEach {
                        val level = it.substringAfterLast(' ')
                        val formattedEnchant = it.replace(" $level", ":${Utils.parseRomanNumeral(level)}")
                        enchantMap.add(formattedEnchant.lowercase().replace(" ", "_"))
                    }
                }
            }
        }
        return enchantMap
    }
}