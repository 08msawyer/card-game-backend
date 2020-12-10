package com.llamalad7.game

class Deck {
    private val cards = allCards.toMutableList().apply { shuffle() }
    fun take() = cards.removeFirst()
    val isEmpty get() = cards.isEmpty()
    val size get() = cards.size
}