package com.example.clientapp.models

import kotlinx.serialization.Serializable

@Serializable
data class GestureCommand(val type: String, val duration: Long)

@Serializable
data class GestureResult(val status: String, val message: String)