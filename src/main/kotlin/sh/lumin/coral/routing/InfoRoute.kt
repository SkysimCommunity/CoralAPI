package sh.lumin.coral.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sh.lumin.coral.utils.API
import sh.lumin.coral.utils.Utils

fun Route.infoRoute() {
    rateLimit(RateLimitName("info")) {
        get("/info/{username}") {
            if(!Utils.validateRequest(call)) {
                return@get
            }

            val username = call.parameters["username"]

            if(username == null || !Utils.isValidUsername(username)) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid or no username was specified."))
                return@get
            }

            val apiKey = call.request.header("Authentication")!!

            call.respond(HttpStatusCode.OK, API.getPlayerInfo(apiKey, username))
        }
    }
}
