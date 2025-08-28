package com.sun.weatherapp

import com.sun.weatherapp.data.reposiroty.AuthRepository
import com.sun.weatherapp.screen.auth.register.RegisterContract
import com.sun.weatherapp.screen.auth.register.RegisterPresenter
import io.mockk.*
import org.junit.Before
import org.junit.Test

class RegisterPresenterTest {

    private val view = mockk<RegisterContract.View>(relaxed = true)
    private val authRepository = mockk<AuthRepository>()
    private lateinit var presenter: RegisterPresenter

    @Before
    fun setUp() {
        presenter = RegisterPresenter(authRepository)
        presenter.attachView(view)
    }

    @Test
    fun `register with mismatched passwords should show error`() {
        presenter.register("user@gmail.com", "123456", "654321")

        verify { view.showError("Passwords do not match.") }
        confirmVerified(view)
    }

    @Test
    fun `register with empty fields should show error`() {
        presenter.register("", "", "")

        verify { view.showError("Fields cannot be empty.") }
        confirmVerified(view)
    }

    @Test
    fun `register success should hide loading and show success`() {
        every {
            authRepository.signUp(any(), any(), onSuccess = captureLambda(), onFailure = any())
        } answers {
            lambda<() -> Unit>().invoke() // gọi onSuccess
        }

        presenter.register("user@gmail.com", "123456", "123456")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showRegisterSuccess()
        }
    }

    @Test
    fun `register failure should hide loading and show error message`() {
        val exception = Exception("Email already exists")

        every {
            authRepository.signUp(any(), any(), onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception)
        }

        presenter.register("user@gmail.com", "123456", "123456")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showError("Email already exists")
        }
    }

    @Test
    fun `register failure with null message should show default error`() {
        val exception = Exception()

        every {
            authRepository.signUp(any(), any(), onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception)
        }

        presenter.register("user@gmail.com", "123456", "123456")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showError("Registration failed.")
        }
    }
}
