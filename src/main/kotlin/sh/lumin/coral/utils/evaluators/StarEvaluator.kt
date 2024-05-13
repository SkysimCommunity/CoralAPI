package sh.lumin.coral.utils.evaluators

import com.google.gson.JsonObject
import sh.lumin.coral.utils.Utils

object StarEvaluator {
    fun valueItem(item: JsonObject): Int {
        //
        val nameElement = item["name"] ?: return 0
        if (nameElement.isJsonNull) return 0
        val name = nameElement.asString ?: return 0
        //
        if(name.count { it == '✪'} == 0) return 0
        //
        val rarity = Utils.getRarityByName(name)
        //
        val stars = name.substringAfterLast(' ', name)
        if(rarity.isBlank()) return 0
        val value = getStarValue(stars)
        //
        return value
    }

    private fun getStarValue(name: String): Int {
        val mm_stars = name.windowed(3).count { it == "§c✪" }
        //
        return when(mm_stars) {
            5 -> 3_000_000 + 2_500_000 + 1_500_000 + 1_000_000 + 500_000
            4 -> 2_500_000 + 1_500_000 + 1_000_000 + 500_000
            3 -> 1_500_000 + 1_000_000 + 500_000
            2 -> 1_000_000 + 500_000
            1 -> 500_000
            else -> 0
        }
    }
}