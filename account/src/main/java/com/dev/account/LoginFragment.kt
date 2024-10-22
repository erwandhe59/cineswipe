package com.dev.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dev.menu.MenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // Views
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: TextView
    private lateinit var noAccountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        signInButton = view.findViewById(R.id.signInButton)
        noAccountTextView = view.findViewById(R.id.noAccountTextView)

        // Set up button click listeners
        signInButton.setOnClickListener { signIn() }
        noAccountTextView.setOnClickListener { navigateToRegisterFragment() }

        return view
    }

    private fun signIn() {
        val input = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (input.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if input is an email or username
        if (input.contains("@")) {
            signInWithEmail(input, password)
        } else {
            signInWithUsername(input, password)
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginFragment", "Connexion réussie avec l'email: $email")
                    navigateToMenuFragment(email)
                } else {
                    Toast.makeText(requireContext(), "Échec de la connexion: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithUsername(username: String, password: String) {
        Log.d("LoginFragment", "Tentative de connexion avec le pseudo : $username")

        // Chercher l'utilisateur par pseudo (normalisé en minuscules)
        firestore.collection("users")
            .whereEqualTo("username", username.lowercase()) // Les pseudos doivent être normalisés en minuscules
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.d("LoginFragment", "Résultats de la requête Firestore : ${querySnapshot.documents}")
                if (!querySnapshot.isEmpty) {
                    // Si un utilisateur est trouvé, récupérer l'email et se connecter avec l'email
                    val userDoc = querySnapshot.documents[0]
                    val email = userDoc.getString("email")
                    Log.d("LoginFragment", "Email récupéré : $email")
                    if (email != null) {
                        signInWithEmail(email, password)
                    }
                } else {
                    // Si aucun utilisateur n'est trouvé, afficher un message
                    Log.d("LoginFragment", "Aucun utilisateur trouvé pour le pseudo : $username")
                    Toast.makeText(requireContext(), "Pseudo introuvable", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Gérer les erreurs de connexion à Firestore
                Log.e("LoginFragment", "Erreur lors de la recherche du pseudo", e)
                Toast.makeText(requireContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show()
            }
    }


    private fun navigateToMenuFragment(email: String) {
        // Récupérer le pseudo de l'utilisateur
        firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDoc = querySnapshot.documents[0]
                    val pseudo = userDoc.getString("username")
                    val menuFragment = MenuFragment().apply {
                        arguments = Bundle().apply {
                            putString("username", pseudo)
                        }
                    }
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.main, menuFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
            .addOnFailureListener { e ->
                Log.w("LoginFragment", "Erreur lors de la récupération du pseudo", e)
                Toast.makeText(requireContext(), "Erreur de récupération du pseudo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToRegisterFragment() {
        val registerFragment = RegisterFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main, registerFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}