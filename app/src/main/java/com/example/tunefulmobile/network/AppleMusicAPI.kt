package com.example.tunefulmobile.network

import com.example.tunefulmobile.data.AppleMusicSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AppleMusicAPI {
    @GET("v1/catalog/us/search")
    suspend fun searchSongs(
        @Header("Authorization") authHeader: String,
        @Query("term") term: String,
        @Query("types") types: String = "songs",
        @Query("limit") limit: Int = 10
    ): AppleMusicSearchResponse
}