package sh.lumin.coral.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import sh.lumin.coral.utils.API
import sh.lumin.coral.utils.Utils
import sh.lumin.coral.utils.evaluators.PlayerEvaluator

fun Route.networthRoute() {
    rateLimit(RateLimitName("networth")) {
        get("/networth/{username}") {
            if(!Utils.validateRequest(call)) {
                return@get
            }

            val username = call.parameters["username"]

            if(username == null || !Utils.isValidUsername(username)) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid or no username was specified."))
                return@get
            }

            val apiKey = call.request.header("Authentication")!!

            call.respond(HttpStatusCode.OK, PlayerEvaluator.valuePlayer(apiKey, username))
        }
    }
}