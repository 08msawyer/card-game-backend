package com.llamalad7

import com.llamalad7.database.Users
import com.llamalad7.database.serializableTransaction
import com.llamalad7.requests.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils

fun main() {
    Database.connect(
        "jdbc:sqlite:database/database.db",
        "org.sqlite.JDBC"
    )

    serializableTransaction {
        SchemaUtils.create(Users)
    }


    embeddedServer(Netty, port = 8080) {
        install(DefaultHeaders)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(CORS) {
            method(HttpMethod.Options)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.Authorization)
            allowCredentials = true
            anyHost() // TODO: Don't do this in production
        }
        routing {
            post("/signup") {
                call.respond(handleSignup(call.receive()))
            }
            post("/login") {
                call.respond(handleLogin(call.receive()))
            }

            post("/newgame") {
                call.respond(handleNewGame(call.receive()))
            }
            post("/takecard") {
                call.respond(handleTakeCard(call.receive()))
            }
            get("/wins") {
                call.respond(handleGetWins(call.receive()))
            }
        }
    }.start(wait = true)
}

