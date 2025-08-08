package com.sun.weatherapp.screen.auth.login

import com.sun.weatherapp.screen.base.BaseContract

interface LoginContract : BaseContract<LoginContract.View, LoginContract.Presenter> {
    interface View : BaseContract.View {
        fun showLoginSuccess()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun login(username: String, password: String)
    }
}
