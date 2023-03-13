package de.vkb.dojo.es.humanResources.services

import jakarta.inject.Singleton
import java.util.*

@Singleton
class ValidatorService {
    private val usernamePattern = Regex("^[a-zA-Z0-9-_@.]+$")
    fun isValidUsername(name: Optional<String>): Boolean {
        return name.map { it: String -> it.isNotBlank() && it.matches(usernamePattern) }.orElse(false)
    }

    fun isValidUsername(name: String?): Boolean {
        return isValidUsername(Optional.ofNullable(name))
    }

    fun isValidFullname(name: Optional<String>): Boolean {
        return name.map { it: String -> it.isNotBlank() }.orElse(false)
    }

    fun isValidFullname(name: String?): Boolean {
        return isValidFullname(Optional.ofNullable(name))
    }
}
