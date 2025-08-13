package com.sun.weatherapp.screen.auth.register

import com.sun.weatherapp.screen.base.BaseContract

interface RegisterContract : BaseContract<RegisterContract.View, RegisterContract.Presenter> {

    interface View : BaseContract.View {
        fun showRegisterSuccess()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun register(username: String, password: String, confirmPassword: String)
    }

}
