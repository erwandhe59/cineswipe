package com.dev.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.dev.home.services.MovieSearchResponse
import com.dev.home.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var descriptionTextView: TextView

    private val imageUrls = mutableListOf<String>()
    private val titles = mutableListOf<String>()

    private val apiKey = "d881e041"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation des vues
        viewPager = view.findViewById(R.id.viewPager)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)

        // Appel à l'API OMDb pour récupérer les films
        fetchMovies()

        // Ajout d'un callback pour détecter le changement de page
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Mettre à jour le titre lorsque l'utilisateur change de page
                updateTitle(view.findViewById(R.id.titleText), titles.getOrNull(position) ?: "")
            }
        })

        // Appliquer le transformateur de page
        viewPager.setPageTransformer(CardTransformer(view.findViewById(R.id.titleText)))
    }

    private fun fetchMovies() {
        val call = RetrofitInstance.api.getMovies(apiKey, "movie")

        call.enqueue(object : Callback<MovieSearchResponse> {
            override fun onResponse(call: Call<MovieSearchResponse>, response: Response<MovieSearchResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.Search ?: emptyList()

                    for (movie in movies) {
                        if (movie.Poster != "N/A") {
                            Log.d("ViewPagerAdapter", "Poster URL: ${movie.Poster}")
                            titles.add(movie.Title)
                            imageUrls.add(movie.Poster)
                        }
                    }

                    viewPagerAdapter = ViewPagerAdapter(imageUrls)
                    viewPager.adapter = viewPagerAdapter

                    // Notifie l'adaptateur des changements
                    viewPagerAdapter.notifyDataSetChanged()

                    // Met à jour le titre initial
                    view?.let { updateTitle(it.findViewById(R.id.titleText), titles.firstOrNull() ?: "") }

                } else {
                    Log.e("HomeFragment", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                Log.e("HomeFragment", "API Call failed", t)
            }
        })
    }

    // Met à jour le titre avec un formatage spécifique
    private fun updateTitle(titleTextView: TextView, title: String) {
        val words = title.split(" ")

        when (words.size) {
            0 -> titleTextView.text = "" // Aucun mot
            1 -> titleTextView.text = title // Un seul mot
            else -> {
                titleTextView.text = "${words[0]} ${words[1]}"
                if (words.size > 2) {
                    titleTextView.append("\n${words.drop(2).joinToString(" ")}")
                }
            }
        }
    }
}