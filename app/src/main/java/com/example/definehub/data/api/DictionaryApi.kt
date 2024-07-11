package com.example.definehub.data.api

import com.example.definehub.data.dto.WordResultDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("{word}")
    suspend fun getWordInfo(
        @Path("word") wordToSearch: String
    ): WordResultDto? = null

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    }

}