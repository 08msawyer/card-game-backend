package com.llamalad7.game

// Maps a user to all users which have sent requests to them
object MatchRequests : MutableMap<String, MutableSet<String>> by mutableMapOf() {
    fun getOrCompute(username: String) = this[username] ?: mutableSetOf<String>().also {
        this[username] = it
    }

    fun removeRequests(player1: String, player2: String) {
        this[player1]?.remove(player2)
        this[player2]?.remove(player1)
    }
}

fun String.hasRequestTo(opponent: String) = this in MatchRequests.getOrCompute(opponent)

fun String.sendRequestTo(opponent: String) = MatchRequests.getOrCompute(opponent).add(this)