package com.llamalad7.game

val allCards = mutableSetOf<Card>().apply {
    for (colour in Colour.values()) {
        for (number in 1..10) {
            add(Card(number, colour))
        }
    }
}

data class Card(val number: Int, val colour: Colour) {
    init {
        require(number in 1..10) {
            "A card's number must be between 1 and 10, but it was $number."
        }
    }

    infix fun beats(other: Card) = when (colour) {
        other.colour -> number > other.number
        Colour.RED -> other.colour == Colour.BLACK
        Colour.BLACK -> other.colour == Colour.YELLOW
        Colour.YELLOW -> other.colour == Colour.RED
    }
}