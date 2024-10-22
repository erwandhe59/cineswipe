package com.dev.menu.instance

import com.dev.home.services.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApi {
    @GET("/")
    fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchQuery: String,
        @Query("type") type: String = "movie" // Type de film par défaut
    ): Call<MovieSearchResponse>
}
