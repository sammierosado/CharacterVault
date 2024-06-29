package com.example.charactervault.network

import com.example.charactervault.model.CharacterDetailResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://www.giantbomb.com/api/"

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GiantBombApi by lazy {
        retrofit.create(GiantBombApi::class.java)
    }
}

interface GiantBombApi {
    @GET("characters/")
    suspend fun getCharacters(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): CharacterResponse

    @GET("character/{id}/")
    suspend fun getCharacterDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): CharacterDetailResponse
}
