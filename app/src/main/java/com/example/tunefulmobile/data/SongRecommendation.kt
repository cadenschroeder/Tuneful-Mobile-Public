package com.example.tunefulmobile.data

import kotlinx.serialization.Serializable

@Serializable
data class SongRecommendation(
    val title: String,
    val artist: String
)