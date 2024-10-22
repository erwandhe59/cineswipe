package com.dev.home.services

import com.dev.home.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDbApiService {
    @GET("/")
    fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchQuery: String, // Utilise "s" pour rechercher tous les films par exemple "batman"
        @Query("type") type: String = "movie"
    ): Call<MovieSearchResponse>

    @GET("/") // Vérifiez si la bonne URL et les paramètres sont nécessaires
    fun getMovieDetails(
        @Query("i") movieId: String,
        @Query("apikey") apiKey: String
    ): Call<MovieDetailsResponse> // Assurez-vous que MovieDetailsResponse est défini
}

data class MovieSearchResponse(
    val Search: List<MovieResponse>
)

// Ajoutez la classe MovieDetailsResponse si elle n'est pas déjà définie
data class MovieDetailsResponse(
    val Title: String?,
    val Year: String?,
    val Rated: String?,
    val Released: String?,
    val Runtime: String?,
    val Genre: String?,
    val Director: String?,
    val Writer: String?,
    val Actors: String?,
    val Plot: String?,
    val Language: String?,
    val Country: String?,
    val Awards: String?,
    val Poster: String?
)
