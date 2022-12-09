package site.uartman

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object Chat {
    val messages: MutableList<Message> = mutableListOf();
    private val _msgs = MutableSharedFlow<Message>()
    val msgs = _msgs.asSharedFlow()
    suspend fun send(message: Message) {
        messages.add(message)
        _msgs.emit(message)
    }
    init {
        CoroutineScope(Job()).launch {
            println("Beginning to collect messages")
              msgs.collect {
                println("[${it.from}]: ${it.text}")
            }
        }
    }
}