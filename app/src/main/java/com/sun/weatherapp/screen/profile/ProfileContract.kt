package com.sun.weatherapp.screen.profile

import android.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import com.sun.weatherapp.screen.base.BaseContract

interface ProfileContract : BaseContract<ProfileContract.View, ProfileContract.Presenter> {

    interface View : BaseContract.View {
        fun showLogoutSuccess()
        fun showDeleteAccountSuccess()
        fun showChangePasswordSuccess()
        fun showUpdateProfileImageSuccess()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun logout()
        fun deleteAccount()
        fun changePassword(newPassword: String, confirmNewPassword: String, dialog: AlertDialog)
        fun getProfileInfo(): FirebaseUser?
        fun updateProfileImage(imageUri: String)
    }
}