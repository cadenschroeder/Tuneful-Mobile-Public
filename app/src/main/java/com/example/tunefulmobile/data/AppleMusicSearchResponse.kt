package com.example.tunefulmobile.data

import kotlinx.serialization.Serializable

@Serializable
data class AppleMusicSearchResponse(
    val results: AppleMusicResults
)

@Serializable
data class AppleMusicResults(
    val songs: AppleMusicSongs
)

@Serializable
data class AppleMusicSongs(
    val data: List<AppleMusicSongData>
)

@Serializable
data class AppleMusicSongData(
    val id: String,
    val attributes: AppleMusicSongAttributes
)

@Serializable
data class AppleMusicSongAttributes(
    val name: String,
    val artistName: String,
    val albumName: String,
    val artwork: Artwork?,
    val previews: List<AppleMusicPreview> = emptyList()
)

@Serializable
data class Artwork(
    val width: Int,
    val height: Int,
    val url: String,
    val bgColor: String
)

@Serializable
data class AppleMusicPreview(
    val url: String
)