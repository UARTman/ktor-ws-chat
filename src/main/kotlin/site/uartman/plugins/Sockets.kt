package site.uartman.plugins

import com.google.gson.Gson
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import site.uartman.Chat
import site.uartman.Message

val gson = Gson()

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/ws") {
            val job = launch {
                Chat.msgs.collect {
                    outgoing.send(Frame.Text(gson.toJson(it)))
                }
            }
            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        val msg: Message = gson.fromJson(text, Message::class.java)
                        Chat.send(msg)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Disconnecting!")
                job.cancel()
            }
        }
    }
}
