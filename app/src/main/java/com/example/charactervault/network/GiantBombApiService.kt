package com.example.charactervault.network

import com.example.charactervault.model.CharacterDetailResponse
import com.example.charactervault.model.CharactersResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiantBombApiService {

    @GET("characters/")
    suspend fun getCharacters(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): CharactersResponse

    @GET("character/{id}/")
    suspend fun getCharacterDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): CharacterDetailResponse
}
