package com.example.tunefulmobile.network

import com.example.tunefulmobile.data.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyAPI {
    @GET("v1/search")
    suspend fun searchTracks(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("type") type: String = "track"
    ): SearchResponse
}