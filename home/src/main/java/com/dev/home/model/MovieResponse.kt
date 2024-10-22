package com.dev.home.model

data class MovieResponse(
    val imdbID: String, // Ajout de l'identifiant IMDb
    val Title: String,
    val Year: String,   // Ajout de l'année de sortie
    val Type: String,   // Ajout du type (film, série, etc.)
    val Poster: String
)
