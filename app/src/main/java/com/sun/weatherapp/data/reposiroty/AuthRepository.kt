package com.sun.weatherapp.data.reposiroty

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.sun.weatherapp.data.service.AuthService

class AuthRepository {
    private val auth = AuthService.auth

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun changePassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            onFailure(Exception("No user logged in"))
        }
    }

    fun deleteAccount(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser
        if (user != null) {
            user.delete()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            onFailure(Exception("No user logged in"))
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun updateProfileImage(
        imageUri: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser
        val uri = Uri.parse(imageUri)
        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()

            user.updateProfile(profileUpdates)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            onFailure(Exception("No user logged in"))
        }
    }
}
