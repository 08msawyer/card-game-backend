package com.llamalad7.requests

import com.llamalad7.game.Card
import com.llamalad7.sessions.Session

data class Response(val ok: Boolean, val message: String)

data class LoginResponse(val ok: Boolean, val message: String, val session: Session)

data class TakeCardResponse(
    val playerOneCard: Card,
    val playerTwoCard: Card,
    val playerOneScore: Int,
    val playerTwoScore: Int,
    val cardsLeft: Int,
    val roundWinner: String
)

data class WinsResponse(val username: String, val wins: Int)