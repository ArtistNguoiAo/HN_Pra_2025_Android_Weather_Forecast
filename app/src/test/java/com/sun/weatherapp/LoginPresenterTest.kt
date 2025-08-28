package com.sun.weatherapp

import com.sun.weatherapp.data.reposiroty.AuthRepository
import com.sun.weatherapp.screen.auth.login.LoginContract
import com.sun.weatherapp.screen.auth.login.LoginPresenter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test

class LoginPresenterTest {

    private val view = mockk<LoginContract.View>(relaxed = true)
    private val authRepository = mockk<AuthRepository>()
    private lateinit var presenter: LoginPresenter

    @Before
    fun setUp() {
        presenter = LoginPresenter(authRepository)
        presenter.attachView(view)
    }

    @Test
    fun `login with empty username and password should show error`() {
        presenter.login("", "")

        verify {
            view.showError("Fields cannot be empty.")
        }
        confirmVerified(view)
    }

    @Test
    fun `login success should hide loading and show success`() {
        every {
            authRepository.signIn(any(), any(), onSuccess = captureLambda(), onFailure = any())
        } answers {
            lambda<() -> Unit>().invoke() // gọi onSuccess
        }

        presenter.login("user@gmail.com", "123456")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showLoginSuccess()
        }
    }

    @Test
    fun `login failure should hide loading and show error message`() {
        val exception = Exception("Invalid credentials")

        every {
            authRepository.signIn(any(), any(), onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception) // gọi onFailure
        }

        presenter.login("user@gmail.com", "wrongpassword")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showError("Invalid credentials")
        }
    }

    @Test
    fun `login failure with null message should show default error`() {
        val exception = Exception()

        every {
            authRepository.signIn(any(), any(), onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception)
        }

        presenter.login("test@gmail.com", "1234567")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showError("Login failed.")
        }
    }
}
