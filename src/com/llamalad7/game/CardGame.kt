package com.llamalad7.game

import com.llamalad7.database.incrementWins

val currentGames = mutableMapOf<String, CardGame>()

class CardGame(private val playerOne: String, private val playerTwo: String) {
    private val deck = Deck()
    var playerOneScore = 0
        private set
    var playerTwoScore = 0
        private set
    val cardsLeft get() = deck.size
    val finished get() = deck.isEmpty
    private var isPlayerOneTurn = true
    lateinit var playerOneLastCard: Card
        private set
    lateinit var playerTwoLastCard: Card
        private set
    lateinit var lastWinner: String
        private set
    var scoreWasUpdated = false
        private set

    fun drawCard(player: String) {
        require(player == playerOne || player == playerTwo) {
            "Player '$player' is not a part of this game"
        }
        val playerOnesTurn = player == playerOne
        if (playerOnesTurn != isPlayerOneTurn) error("It is not your turn")
        if (playerOnesTurn) {
            scoreWasUpdated = false
            playerOneLastCard = deck.take()
        } else {
            val card = deck.take()
            lastWinner = if (card beats playerOneLastCard) {
                playerTwoScore++
                playerTwo
            } else {
                playerOneScore++
                playerOne
            }
            playerTwoLastCard = card
            scoreWasUpdated = true
        }
        isPlayerOneTurn = !isPlayerOneTurn
        if (finished) {
            currentGames.remove(playerOne, this)
            currentGames.remove(playerTwo, this)
            incrementWins(
                if (playerOneScore > playerTwoScore) playerOne else playerTwo
            )
        }
    }
}

fun startGame(playerOne: String, playerTwo: String) {
    val game = CardGame(playerOne, playerTwo)
    currentGames[playerOne] = game
    currentGames[playerTwo] = game
}