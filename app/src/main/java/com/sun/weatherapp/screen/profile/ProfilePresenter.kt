package com.sun.weatherapp.screen.profile

import android.app.AlertDialog
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.sun.weatherapp.data.reposiroty.AuthRepository
import com.sun.weatherapp.screen.base.BasePresenter

class ProfilePresenter (
    private val authRepository: AuthRepository
): BasePresenter<ProfileContract.View>(), ProfileContract.Presenter {

    override fun logout() {
        authRepository.signOut()
        getView()?.showLogoutSuccess()
    }

    override fun deleteAccount() {
        authRepository.deleteAccount(
            onSuccess = {
                getView()?.showDeleteAccountSuccess()
            },
            onFailure = { exception ->
                getView()?.showError(exception.message ?: "Failed to delete account.")
            }
        )
    }

    override fun changePassword(newPassword: String, confirmNewPassword: String, dialog: AlertDialog) {
        if (newPassword != confirmNewPassword) {
            getView()?.showError("Passwords do not match.")
        }
        else if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            getView()?.showError("Fields cannot be empty.")
        }
        else {
            getView()?.showLoading()
            authRepository.changePassword(
                newPassword = newPassword,
                onSuccess = {
                    getView()?.hideLoading()
                    getView()?.showChangePasswordSuccess()
                    dialog.dismiss()
                },
                onFailure = { exception ->
                    getView()?.hideLoading()
                    getView()?.showError(exception.message ?: "Failed to change password.")
                }
            )
        }
    }

    override fun getProfileInfo(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    override fun updateProfileImage(imageUri: String) {
        authRepository.updateProfileImage(
            imageUri = imageUri,
            onSuccess = {
                getView()?.showUpdateProfileImageSuccess()
            },
            onFailure = { exception ->
                Log.e("ProfilePresenter", "Failed to update profile image: ${exception.message}")
                getView()?.showError(exception.message ?: "Failed to update profile image.")
            }
        )
    }
}