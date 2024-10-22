package com.dev.menu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class MovieInfoDialogFragment : DialogFragment() {

    // Propriété pour stocker l'URL de l'image
    private var featuredImageUrl: String? = null

    // Méthode pour recevoir l'URL de l'image
    fun setFeaturedImageUrl(url: String?) { // Changer String à String?
        featuredImageUrl = url
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // Fond transparent
        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_movie_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.95).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setDimAmount(0.7f) // Ajuster l'opacité
        val view = dialog?.window?.decorView
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_in)
        view?.startAnimation(animation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogView = requireView()
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_in)
        dialogView.startAnimation(animation)

        val closeButton: Button = view.findViewById(R.id.close_button)
        val infoTextView: TextView = view.findViewById(R.id.info_text)
        val titleTextView: TextView = view.findViewById(R.id.info_title)

        // Mettre le contenu de l'article ici
        titleTextView.text = "Terrifier 3"
        infoTextView.text = "Terrifier 3 arrive terriblement dans les salles de cinéma ! \nPréparez-vous pour une expérience d'horreur inoubliable. \nNe manquez pas la première qui s'annonce déjà comme un véritable événement cinématographique."

        closeButton.setOnClickListener {
            closeDialog() // Ferme le dialog avec animation
        }
    }

    // Nouvelle méthode pour fermer le dialog avec animation
    private fun closeDialog() {
        val dialogView = requireView()
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_out)
        dialogView.startAnimation(animation)

        dialogView.postDelayed({
            dismiss() // Ferme le dialog
        }, 300)
    }
}