package com.example.tunefulmobile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val tracks: Tracks
)

@Serializable
data class Tracks(
    val items: List<Track>
)

@Serializable
data class Track(
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    @SerialName("preview_url")
    val previewUrl: String? = null
)

@Serializable
data class Artist(
    val name: String
)

@Serializable
data class Album(
    val name: String,
    @SerialName("images") val images: List<Image>
)

@Serializable
data class Image(
    val url: String
)