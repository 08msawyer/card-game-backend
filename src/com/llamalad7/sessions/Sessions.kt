package com.llamalad7.sessions

import java.time.Instant
import java.util.*

val currentSessions = mutableMapOf<String, Session>()

data class Session constructor(val username: String, val id: UUID, val expiry: Instant) {
    val hasExpired
        get() = if (Instant.now() > expiry) {
            currentSessions.remove(username)
            true
        } else false

    val isValid
        get() = !hasExpired && currentSessions[username] == this
}

fun newSession(username: String) =
    Session(username, UUID.randomUUID(), Instant.now().plusSeconds(1800)).also {
        currentSessions[username] = it
    }

fun getSession(username: String) = currentSessions[username]?.takeIf { it.isValid }