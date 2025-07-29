package com.example.tunefulmobile.di

import com.example.tunefulmobile.network.AppleMusicAPI
import com.example.tunefulmobile.network.SpotifyAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpotifyAPIHost

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppleAPIHost

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @SpotifyAPIHost
    @Singleton
    fun provideSpotifyRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }
                    .asConverterFactory("application/json".toMediaType())
            )
            .client(client)
            .build()
    }

    @Provides
    @AppleAPIHost
    @Singleton
    fun provideAppleMusicRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl("https://api.music.apple.com/")
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }
                    .asConverterFactory("application/json".toMediaType())
            )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAPI(@SpotifyAPIHost retrofit: Retrofit): SpotifyAPI {
        return retrofit.create(SpotifyAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAppleMusicAPI(@AppleAPIHost retrofit: Retrofit): AppleMusicAPI {
        return retrofit.create(AppleMusicAPI::class.java)
    }
}