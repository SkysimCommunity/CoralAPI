package sh.lumin.coral

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.ratelimit.*
import sh.lumin.coral.plugins.configureHTTP
import sh.lumin.coral.plugins.configureRouting
import sh.lumin.coral.plugins.configureSerialization
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(RateLimit) {
        register(RateLimitName("items")) { // 5s cooldown
            rateLimiter(limit = 12, refillPeriod = 60.seconds)
        }
        //
        register(RateLimitName("networth")) { // 6s cooldown
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
        }
        //
        register(RateLimitName("info")) { //  4s cooldown
            rateLimiter(limit = 15, refillPeriod = 60.seconds)
        }
    }
    configureHTTP()
    configureSerialization()
    configureRouting()
}
