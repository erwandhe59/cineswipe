package com.dev.home

import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2

class CardTransformer(private val titleTextView: TextView) : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        when {
            position < -1 -> { // La page est complètement à gauche
                view.alpha = 0f
                view.translationX = -view.width.toFloat()
                view.rotationY = 0f // Pas de rotation
                titleTextView.alpha = 0f // Masquer le titre
                titleTextView.translationX = -view.width.toFloat() // Masquer le titre
            }
            position <= 1 -> { // La page est visible
                // Transparence de la carte
                view.alpha = 1 - Math.abs(position) // Estompe la carte

                // Translation de la carte
                view.translationX = position * view.width // Déplacement horizontal
                view.translationY = position * 40 // Déplacement vertical pour l'impact

                // Rotation de la carte avec un effet de profondeur
                view.rotationY = position * -30 // Rotation de 30 degrés max pour un effet captivant

                // Animation du titre : Accélérer l'estompage
                titleTextView.alpha = Math.max(0f, 1 - Math.abs(position * 2)) // Multipliez par 2 pour un estompage plus rapide
                titleTextView.translationX = position * view.width * 0.6f // Déplacement du titre
                titleTextView.translationY = position * 20 // Translation verticale du titre

                // Échelle pour un effet d'approche plus prononcé
                val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f // Scale ajusté
                view.scaleY = scaleFactor
                view.scaleX = scaleFactor

                // Ajout d'une ombre pour un effet de profondeur
                view.elevation = 10 - Math.abs(position) * 5 // Élévation diminue avec l'éloignement
            }
            else -> { // La page est complètement à droite
                view.alpha = 0f
                view.translationX = view.width.toFloat()
                view.rotationY = 0f // Pas de rotation
                titleTextView.alpha = 0f // Masquer le titre
                titleTextView.translationX = view.width.toFloat() // Masquer le titre
            }
        }
    }
}