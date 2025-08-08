package com.sun.weatherapp.screen.auth.login

import com.sun.weatherapp.screen.base.BasePresenter

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun login(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            getView()?.showLoginSuccess()
        } else {
            getView()?.showError("Login successful for user: $username")
        }
    }
}
