package de.vkb.dojo.es.humanResources.model.state

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Person(
    val username: String,
    val fullname: String,
    val sick: Boolean
)
