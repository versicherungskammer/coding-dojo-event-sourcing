package de.vkb.dojo.es.facilityManagement.model.state

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Room(
    val name: String,
    val maintenance: Boolean
)
