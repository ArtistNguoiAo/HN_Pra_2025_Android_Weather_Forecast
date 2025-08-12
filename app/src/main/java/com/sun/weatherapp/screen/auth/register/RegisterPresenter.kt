package com.sun.weatherapp.screen.auth.register

import com.sun.weatherapp.screen.base.BasePresenter

class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    override fun register(username: String, password: String, confirmPassword: String) {
        if (username.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
            getView()?.showRegisterSuccess()
        } else {
            getView()?.showError("Registration failed. Please check your input.")
        }
    }

}
