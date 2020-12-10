package com.llamalad7.requests

import com.llamalad7.sessions.Session

data class User(val username: String, val password: String) {
    val hasValidUsername: Boolean
        get() {
            if (username.length !in 5..16) return false
            for (char in username) {
                if (char !in 'A'..'Z' && char !in 'a'..'z' && char !in '0'..'9' && char != '_') return false
            }
            return true
        }

    val hasValidPassword: Boolean
        get() {
            if (password.length !in 8..32) return false
            val checks = arrayOf(false, false, false, false)
            for (char in password) {
                checks[when (char) {
                    in 'a'..'z' -> 0
                    in 'A'..'Z' -> 1
                    in '0'..'9' -> 2
                    else -> 3
                }] = true
                if (checks.all { it }) return true
            }
            return false
        }
}

data class MultiplayerGameRequest(val opponent: String, val session: Session)

data class TakeCardRequest(val session: Session)

data class LeaderboardRequest(val username: String)