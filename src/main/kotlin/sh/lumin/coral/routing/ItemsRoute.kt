package sh.lumin.coral.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sh.lumin.coral.utils.API
import sh.lumin.coral.utils.Utils

fun Route.itemsRoute() {
    rateLimit(RateLimitName("items")) {
        get("/items/{username}") {
            if (!Utils.validateRequest(call)) {
                return@get
            }

            val username = call.parameters["username"]

            if(username == null || !Utils.isValidUsername(username)) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid or no username was specified."))
                return@get
            }
            //
            val apiKey = call.request.header("Authentication")!!
            //
            val type = call.request.queryParameters["type"]
            //
            val additional = mutableMapOf<String, Any>()
            //
            if(call.parameters["withInfo"] != null) {
                additional["info"] = API.getPlayerInfo(apiKey, username)
            }
            //
            if(type == null) {
                call.respond(HttpStatusCode.OK, API.getPlayerItems(apiKey, username) + additional)
            } else {
                if(!API.ITEM_TYPES.contains(type)) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Unknown type parameter!"))
                    return@get
                }
                //
                call.respond(HttpStatusCode.OK, API.getPlayerItems(apiKey, username, type) + additional)
            }
        }
    }
}