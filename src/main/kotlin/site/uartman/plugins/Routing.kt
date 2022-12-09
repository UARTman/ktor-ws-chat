package site.uartman.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import site.uartman.Chat
import site.uartman.Message

fun Application.configureRouting() {


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/chat") {
            call.respond(Chat.messages)
        }
        post("/chat") {
            val msg: Message = call.receive()
            Chat.send(msg)
            call.respond(Chat.messages)
        }
        get("/chat/send") {
            Chat.send(Message("Server", "Testing..."))
            call.respond(Chat.messages)
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
