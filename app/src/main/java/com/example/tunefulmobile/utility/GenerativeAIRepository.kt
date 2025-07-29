package com.example.tunefulmobile.utility

import com.example.tunefulmobile.secrets.ApiKeys
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.generationConfig

class GenerativeAiRepository {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = ApiKeys.ANDROID_STUDIO_KEY,
        generationConfig = generationConfig {
            this.temperature = 0.9f
//            this.topK = 32
//            this.topP = 0.95f
        }
    )

    suspend fun generateContent(prompt: String): String {
        return try {
            generativeModel.generateContent(
                prompt
                ).text ?: "No response"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}