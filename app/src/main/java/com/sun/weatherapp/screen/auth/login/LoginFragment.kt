package com.sun.weatherapp.screen.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.weatherapp.databinding.FragmentLoginBinding
import com.sun.weatherapp.screen.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginPresenter>(), LoginContract.View {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        presenter = LoginPresenter()
    }

    override fun setupViews() {
        presenter?.attachView(this)
    }

    override fun setupListeners() {
    }

    override fun showLoginSuccess() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(message: String) {
    }
}