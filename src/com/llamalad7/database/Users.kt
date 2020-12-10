package com.llamalad7.database

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val username = text("username")
    override val primaryKey = PrimaryKey(username)
    val password = text("password")
    val wins = integer("wins")
}