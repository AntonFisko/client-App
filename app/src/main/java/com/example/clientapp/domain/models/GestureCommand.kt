package com.example.clientapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class GestureCommand(
    val type: String,
    val duration: Long
)