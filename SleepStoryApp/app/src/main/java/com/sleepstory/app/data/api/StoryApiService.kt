package com.sleepstory.app.data.api

import com.sleepstory.app.data.model.StoryContent
import com.sleepstory.app.data.model.StoryGenerationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StoryApiService {
    
    @POST("api/stories/generate")
    suspend fun generateStory(
        @Body request: StoryGenerationRequest
    ): Response<StoryContent>
    
    @POST("api/stories/tts")
    suspend fun generateTTS(
        @Body text: String
    ): Response<TTSResponse>
}

data class TTSResponse(
    val audioUrl: String,
    val duration: Int
)