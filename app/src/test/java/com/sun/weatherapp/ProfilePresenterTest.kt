package com.sun.weatherapp

import android.app.AlertDialog
import com.sun.weatherapp.data.reposiroty.AuthRepository
import com.sun.weatherapp.screen.profile.ProfileContract
import com.sun.weatherapp.screen.profile.ProfilePresenter
import io.mockk.*
import org.junit.Before
import org.junit.Test

class ProfilePresenterTest {

    private val view = mockk<ProfileContract.View>(relaxed = true)
    private val authRepository = mockk<AuthRepository>()
    private lateinit var presenter: ProfilePresenter

    @Before
    fun setUp() {
        presenter = ProfilePresenter(authRepository)
        presenter.attachView(view)
    }

    @Test
    fun `logout should call repository signOut and show logout success`() {
        every { authRepository.signOut() } just Runs

        presenter.logout()

        verify {
            authRepository.signOut()
            view.showLogoutSuccess()
        }
    }

    @Test
    fun `delete account success should show delete account success`() {
        every {
            authRepository.deleteAccount(onSuccess = captureLambda(), onFailure = any())
        } answers {
            lambda<() -> Unit>().invoke()
        }

        presenter.deleteAccount()

        verify { view.showDeleteAccountSuccess() }
    }

    @Test
    fun `delete account failure should show error`() {
        val exception = Exception("Delete failed")
        every {
            authRepository.deleteAccount(onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception)
        }

        presenter.deleteAccount()

        verify { view.showError("Delete failed") }
    }

    @Test
    fun `change password with mismatched passwords should show error`() {
        val dialog = mockk<AlertDialog>(relaxed = true)

        presenter.changePassword("123456", "654321", dialog)

        verify { view.showError("Passwords do not match.") }
    }

    @Test
    fun `change password with empty fields should show error`() {
        val dialog = mockk<AlertDialog>(relaxed = true)

        presenter.changePassword("", "", dialog)

        verify { view.showError("Fields cannot be empty.") }
    }

    @Test
    fun `change password success should hide loading, show success and dismiss dialog`() {
        val dialog = mockk<AlertDialog>(relaxed = true)

        every {
            authRepository.changePassword(any(), onSuccess = captureLambda(), onFailure = any())
        } answers {
            lambda<() -> Unit>().invoke()
        }

        presenter.changePassword("123456", "123456", dialog)

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showChangePasswordSuccess()
            dialog.dismiss()
        }
    }

    @Test
    fun `change password failure should hide loading and show error`() {
        val dialog = mockk<AlertDialog>(relaxed = true)
        val exception = Exception("Change failed")

        every {
            authRepository.changePassword(any(), onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception)
        }

        presenter.changePassword("123456", "123456", dialog)

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.showError("Change failed")
        }
    }

    @Test
    fun `update profile image success should show update success`() {
        every {
            authRepository.updateProfileImage(any(), onSuccess = captureLambda(), onFailure = any())
        } answers {
            lambda<() -> Unit>().invoke()
        }

        presenter.updateProfileImage("imageUri")

        verify { view.showUpdateProfileImageSuccess() }
    }

    @Test
    fun `update profile image failure should show error`() {
        val exception = Exception("Image upload failed")
        every {
            authRepository.updateProfileImage(any(), onSuccess = any(), onFailure = captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception)
        }

        presenter.updateProfileImage("imageUri")

        verify { view.showError("Image upload failed") }
    }
}
