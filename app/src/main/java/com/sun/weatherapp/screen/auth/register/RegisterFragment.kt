package com.sun.weatherapp.screen.auth.register

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.weatherapp.databinding.FragmentRegisterBinding
import com.sun.weatherapp.screen.base.BaseFragment

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterPresenter>(),
    RegisterContract.View {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        presenter = RegisterPresenter()
    }

    override fun setupViews() {
        presenter?.attachView(this)
    }

    override fun setupListeners() {
    }

    override fun showRegisterSuccess() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(message: String) {
    }

}
