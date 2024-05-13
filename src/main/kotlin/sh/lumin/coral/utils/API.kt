package sh.lumin.coral.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request

object API {
    val ITEM_TYPES = listOf("inventory", "equipments", "enderchest", "armor", "wardrobe", "vaults", "pets")
    private val client = OkHttpClient()
    private const val baseURL = "https://api.skysim.sbs/"
    private val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create()
    //
    fun getPlayerItems(apiKey: String, name: String): Map<String, JsonArray> {
        val request = Request.Builder()
            .get()
            .url("$baseURL?key=$apiKey&type=PLAYER_ITEMS&param=$name")
            .build()

        val response = client.newCall(request).execute()
        //
        val items = gson.fromJson(response.body?.string(), JsonObject::class.java)
        //
        val itemMap = mutableMapOf<String, JsonArray>()
        //
        for(type in ITEM_TYPES) {
            val item = items.getAsJsonArray(type)
            if(item != null) {
                itemMap[type] = item
            }
        }
        //
        return itemMap
    }
    //
    fun getPlayerMoney(apiKey: String, name: String): Triple<Long, Double, Double> {
        val request = Request.Builder().get()
            .url("$baseURL?key=$apiKey&type=PLAYER_INFO&param=$name").build()
        val response = client.newCall(request).execute()
        //
        val info = gson.fromJson(response.body?.string(), JsonObject::class.java)
        //
        return Triple(
            info.get("bits").asLong,
            info.get("coins").asDouble,
            info.get("bankCoins").asDouble
        )
    }
    //
    fun getPlayerItems(apiKey: String, name: String, type: String): Map<String, JsonArray> {
        val request = Request.Builder()
            .get()
            .url("$baseURL?key=$apiKey&type=PLAYER_ITEMS&param=$name")
            .build()

        val response = client.newCall(request).execute()
        //
        val items = gson.fromJson(response.body?.string(), JsonObject::class.java)
        //
        val itemMap = mutableMapOf<String, JsonArray>()
        //
        val item = items.getAsJsonArray(type)
        if(item != null) {
            itemMap[type] = item
        }
        //
        return itemMap
    }
    //
    fun getPlayerInfo(apiKey: String, name: String): Map<String, Any> {
        val request = Request.Builder().get()
            .url("$baseURL?key=$apiKey&type=PLAYER_INFO&param=$name").build()
        val response = client.newCall(request).execute()
        //
        val info = gson.fromJson(response.body?.string()?.removeFormatting(), Map::class.java)
        //
        return info as Map<String, Any>
    }
    //
    fun getPlayerBits(apiKey: String, name: String): Long {
        val request = Request.Builder().get()
            .url("$baseURL?key=$apiKey&type=PLAYER_INFO&param=$name").build()
        val response = client.newCall(request).execute()
        //
        val info = gson.fromJson(response.body?.string(), JsonObject::class.java)
        //
        return info.get("bits").asLong
    }
}
