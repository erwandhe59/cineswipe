package com.dev.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dev.home.services.MovieDetailsResponse
import com.dev.home.services.MovieSearchResponse
import com.dev.home.services.RetrofitInstance
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment() {

    private lateinit var welcomeTextView: TextView
    private lateinit var movieContainer: LinearLayout

    private var username: String? = null
    private lateinit var languages: Array<String>
    private var imageUrl: String? = null // Ajoutez cette ligne

    private val apiKey = "d881e041"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Récupérer le nom d'utilisateur si passé
        arguments?.let {
            username = it.getString("username")
        }

        // Initialiser les messages de bienvenue
        languages = arrayOf(
            "Bonjour", "Hello", "Hi", "T'es re là ?", "Re-bienvenue",
            "What's up ?", "Yo", "Salut", "Encore toi ?"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gonfler le layout pour ce fragment
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser les vues
        welcomeTextView = view.findViewById(R.id.welcomeText)
        movieContainer = view.findViewById(R.id.movieContainer)
        val featuredImage: ShapeableImageView = view.findViewById(R.id.featuredImage) // Assurez-vous d'importer la bonne image

        // Définir le message de bienvenue
        val randomGreeting = languages.random()
        val formattedGreeting = if (randomGreeting.endsWith("?")) {
            "$randomGreeting ${username ?: "utilisateur"}"
        } else {
            "$randomGreeting, ${username ?: "utilisateur"} !"
        }
        welcomeTextView.text = formattedGreeting

        // Gérer le clic sur l'image
        featuredImage.setOnClickListener {
            // Ouvrir le dialog avec l'image
            val dialog = MovieInfoDialogFragment()
            dialog.setFeaturedImageUrl(imageUrl) // Assurez-vous que `imageUrl` est défini
            dialog.show(parentFragmentManager, "MovieInfoDialog")
        }

        // Récupérer les films et les afficher dans le HorizontalScrollView
        fetchHorrorMovies()
    }


    private fun fetchHorrorMovies() {
        // Appel API pour récupérer les films d'horreur
        val call = RetrofitInstance.api.getMovies(apiKey, "horror", "movie")

        call.enqueue(object : Callback<MovieSearchResponse> {
            override fun onResponse(call: Call<MovieSearchResponse>, response: Response<MovieSearchResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.Search ?: emptyList()

                    // Effacer le conteneur précédent
                    movieContainer.removeAllViews()

                    // Créer des CardView pour chaque affiche de film
                    for (movie in movies) {
                        if (movie.Poster != "N/A") {
                            val cardView = layoutInflater.inflate(R.layout.item_movie_poster, movieContainer, false)
                            val imageView: ImageView = cardView.findViewById(R.id.moviePosterImage)

                            Glide.with(imageView.context)
                                .load(movie.Poster)
                                .into(imageView)

                            // Définir l'URL de l'image
                            imageUrl = movie.Poster // Enregistrer l'URL de l'image

                            // Ajoute un clic à chaque poster pour afficher les détails
                            cardView.setOnClickListener {
                                showMovieInfoDialog(movie.imdbID) // Passer l'imdbID du film
                            }

                            movieContainer.addView(cardView)
                        }
                    }
                } else {
                    Log.e("MenuFragment", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                Log.e("MenuFragment", "API Call failed", t)
            }
        })
    }

    private fun showMovieInfoDialog(movieId: String) {
        fetchMovieDetails(movieId) // Passez l'ID du film pour obtenir les détails
    }

    private fun fetchMovieDetails(movieId: String) {
        val call = RetrofitInstance.api.getMovieDetails(movieId, apiKey) // Assure-toi que cette méthode est définie dans ton API
        call.enqueue(object : Callback<MovieDetailsResponse> { // Remplace MovieDetailsResponse par la classe que tu utilises pour les détails du film
            override fun onResponse(call: Call<MovieDetailsResponse>, response: Response<MovieDetailsResponse>) {
                if (response.isSuccessful) {
                    val movieDetails = response.body() // Obtiens les détails du film
                    val dialog = MovieDetailDialogFragment()

                    // Passe les détails du film au dialog
                    val bundle = Bundle().apply {
                        putString("Title", movieDetails?.Title)
                        putString("Year", movieDetails?.Year)
                        putString("Rated", movieDetails?.Rated)
                        putString("Released", movieDetails?.Released)
                        putString("Runtime", movieDetails?.Runtime)
                        putString("Genre", movieDetails?.Genre)
                        putString("Director", movieDetails?.Director)
                        putString("Writer", movieDetails?.Writer)
                        putString("Actors", movieDetails?.Actors)
                        putString("Plot", movieDetails?.Plot)
                        putString("Language", movieDetails?.Language)
                        putString("Country", movieDetails?.Country)
                        putString("Awards", movieDetails?.Awards)
                        putString("Poster", movieDetails?.Poster)
                    }

                    dialog.arguments = bundle // Passe les arguments au dialog
                    dialog.show(parentFragmentManager, "MovieDetailDialog")
                } else {
                    Log.e("MenuFragment", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                Log.e("MenuFragment", "API Call failed", t)
            }
        })
    }
}