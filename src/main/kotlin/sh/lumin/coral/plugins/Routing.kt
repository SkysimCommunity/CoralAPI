package sh.lumin.coral.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import sh.lumin.coral.routing.*
fun Application.configureRouting() {
    routing {
        networthRoute()
        itemsRoute()
        infoRoute()
    }
}
