package sh.lumin.coral.utils.evaluators


import sh.lumin.coral.utils.API

object PlayerEvaluator {
    private val itemTypes = listOf("inventory", "equipments", "enderchest", "armor", "wardrobe", "pets")

    fun valuePlayer(apiKey: String, name: String): Map<String, Long> {

        val items = API.getPlayerItems(apiKey, name)
        val valueMap = mutableMapOf<String, Long>()
        //
        for (type in itemTypes) {
            val itemList = items[type]
            val totalValue = itemList?.filterNot { it.isJsonNull }?.sumOf {
                ItemEvaluator.valueItem(it.asJsonObject)
            } ?: 0

            valueMap[type] = totalValue.toLong()
        }
        // Calculate value for vaults
        var vaultValue = 0L
        items["vaults"]?.let { vaults ->
            vaults.asJsonArray.forEach { subvault ->
                subvault.asJsonObject.get("items").asJsonArray?.let { itemList ->
                    vaultValue += itemList.filterNot { item -> item.isJsonNull }.sumOf { item ->
                        ItemEvaluator.valueItem(item.asJsonObject)
                    }
                }
            }
        }
        //
        valueMap["vaults"] = vaultValue
        //
        valueMap["purse"] = API.getPlayerBits(apiKey, name)
        // Calculate total value
        val totalValue = valueMap.values.sum()
        valueMap["totalValue"] = totalValue
        //
        return valueMap
    }
}