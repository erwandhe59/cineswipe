package com.dev.menu

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import androidx.fragment.app.DialogFragment

class MovieDetailDialogFragment : DialogFragment() {

    private var isDescriptionExpanded = false // État de l'expansion

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Créer le Dialog
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // Fond transparent
        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_movie_details, container, false)
    }

    override fun onStart() {
        super.onStart()

        // Définir la largeur à 95% de l'écran
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.95).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        // Appliquer l'effet de flou à l'arrière-plan
        dialog?.window?.setDimAmount(0.7f) // Ajuster l'opacité
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton: Button = view.findViewById(R.id.close_button)
        val titleTextView: TextView = view.findViewById(R.id.info_title)
        val infoTextView: TextView = view.findViewById(R.id.info_text)
        val posterImageView: ShapeableImageView = view.findViewById(R.id.movie_image)

        // Récupérer les détails du film depuis les arguments
        arguments?.let {
            titleTextView.text = it.getString("Title")
            infoTextView.text = """
                Year: ${it.getString("Year")}
                Rated: ${it.getString("Rated")}
                Released: ${it.getString("Released")}
                Runtime: ${it.getString("Runtime")}
                Genre: ${it.getString("Genre")}
                Director: ${it.getString("Director")}
                Writer: ${it.getString("Writer")}
                Actors: ${it.getString("Actors")}
                Plot: ${it.getString("Plot")}
                Language: ${it.getString("Language")}
                Country: ${it.getString("Country")}
                Awards: ${it.getString("Awards")}
            """.trimIndent()

            // Charger l'image du film
            val posterUrl = it.getString("Poster")
            Glide.with(posterImageView.context)
                .load(posterUrl)
                .into(posterImageView)
        }

        // Ajouter un clic sur la description pour l'agrandir ou la réduire
        infoTextView.setOnClickListener {
            if (isDescriptionExpanded) {
                // Réduire la description
                infoTextView.maxLines = 4 // Limite à 4 lignes
                infoTextView.ellipsize = TextUtils.TruncateAt.END // Afficher "..." à la fin
                posterImageView.visibility = View.VISIBLE // Affiche l'image
            } else {
                // Agrandir la description
                infoTextView.maxLines = Int.MAX_VALUE // Affiche tout le texte
                posterImageView.visibility = View.GONE // Cache l'image
            }
            isDescriptionExpanded = !isDescriptionExpanded // Inverser l'état
        }

        closeButton.setOnClickListener {
            dismiss() // Ferme le dialog
        }
    }
}