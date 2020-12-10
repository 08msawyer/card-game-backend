package com.llamalad7.requests

import com.llamalad7.database.Users
import com.llamalad7.database.Users.password
import com.llamalad7.database.Users.wins
import com.llamalad7.database.getUser
import com.llamalad7.database.serializableTransaction
import com.llamalad7.database.userExists
import com.llamalad7.game.*
import com.llamalad7.sessions.getSession
import com.llamalad7.sessions.newSession
import org.jetbrains.exposed.sql.insert
import org.mindrot.jbcrypt.BCrypt

fun handleSignup(user: User) = when {
    userExists(user.username) -> Response(false, "User already exists")
    !user.hasValidUsername -> Response(
        false,
        "Username must be between 5 and 16 characters, and consist of only A-z, 0-9 and _"
    )
    !user.hasValidPassword -> Response(
        false,
        "Password must be between 8 and 32 characters, and contain at least one lowercase letter, uppercase letter, digit, and special character"
    )
    else -> {
        serializableTransaction {
            Users.insert {
                it[username] = user.username
                it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt())
                it[wins] = 0
            }
        }
        Response(true, "Sign-up successful")
    }
}

fun handleLogin(user: User): Any {
    val userEntry = getUser(user.username) ?: return Response(false, "User doesn't exist")
    return if (BCrypt.checkpw(user.password, userEntry[password])) {
        LoginResponse(true, "Correct password", newSession(user.username))
    } else {
        Response(false, "Incorrect password")
    }
}

fun handleNewGame(gameRequest: MultiplayerGameRequest): Any {
    val username = gameRequest.session.username
    val opponent = gameRequest.opponent
    return when {
        !gameRequest.session.isValid -> Response(false, "Invalid session")
        getSession(opponent) == null -> Response(false, "Opponent doesn't exist or is not online")
        username.hasRequestTo(opponent) -> Response(
            false,
            "You already have an existing request to that user"
        )
        opponent.hasRequestTo(username) -> {
            MatchRequests.removeRequests(username, opponent)
            startGame(username, opponent)
            Response(true, "Request accepted. Game started")
        }
        else -> {
            username.sendRequestTo(opponent)
            Response(true, "Request sent successfully")
        }
    }
}

fun handleTakeCard(request: TakeCardRequest): Any {
    return when {
        !request.session.isValid -> Response(false, "Invalid Session")
        else -> {
            val game = currentGames[request.session.username] ?: return Response(
                false,
                "You don't currently have an active game"
            )
            try {
                with(game) {
                    drawCard(request.session.username)
                    while (!scoreWasUpdated) Thread.sleep(500)
                    TakeCardResponse(
                        playerOneLastCard,
                        playerTwoLastCard,
                        playerOneScore,
                        playerTwoScore,
                        cardsLeft,
                        lastWinner
                    )
                }
            } catch (e: Exception) {
                Response(false, e.message ?: "An internal error occurred")
            }
        }
    }
}

fun handleGetWins(request: LeaderboardRequest): Any {
    val entry = getUser(request.username) ?: return Response(false, "User doesn't exist")
    return WinsResponse(request.username, entry[wins])
}