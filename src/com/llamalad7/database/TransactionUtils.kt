package com.llamalad7.database

import com.llamalad7.database.Users.username
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.sql.Connection

fun <T> serializableTransaction(block: Transaction.() -> T): T {
    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1, null, block)
}

fun getUser(name: String) = serializableTransaction {
    Users.select { username eq name }.firstOrNull()
}

fun userExists(name: String) = getUser(name) != null

fun incrementWins(name: String): Unit = serializableTransaction {
    Users.update({ username eq name }) {
        it.update(wins) { wins + 1 }
    }
}

fun deleteUser(name: String): Unit = serializableTransaction {
    Users.deleteWhere { username eq name }
}