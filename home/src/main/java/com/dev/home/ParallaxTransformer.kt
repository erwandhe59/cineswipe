package com.dev.home

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ParallaxTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        // Effet de parallax
        val pageWidth = view.width

        when {
            position < -1 -> { // La page est complètement à gauche
                view.alpha = 0f // Masquer la vue
            }
            position <= 1 -> { // La page est visible
                // Appliquer l'effet de translation
                view.translationX = -position * pageWidth / 2 // Déplacer la page selon sa position

                // Ajuster la transparence
                view.alpha = 1 - Math.abs(position)

                // Ajuster la mise à l'échelle
                val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f // Scale
                view.scaleY = scaleFactor
                view.scaleX = scaleFactor
            }
            else -> { // La page est complètement à droite
                view.alpha = 0f // Masquer la vue
            }
        }
    }
}