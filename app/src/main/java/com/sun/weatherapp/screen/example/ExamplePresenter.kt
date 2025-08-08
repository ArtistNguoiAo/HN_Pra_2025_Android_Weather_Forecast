package com.sun.weatherapp.screen.example

import com.sun.weatherapp.data.reposiroty.FirebaseRepository
import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExamplePresenter(
    private val teamRepository: ITeamRepository,
    private val firebaseRepository: FirebaseRepository
) : BasePresenter<ExampleContract.View>(), ExampleContract.Presenter {

    override fun loadMyTeams() {
        presenterScope.launch {
            val teams = try {
                withContext(Dispatchers.IO) {
                    teamRepository.getMyTeams()
                }
            } catch (e: Exception) {
                getView()?.showError("Failed to load teams: ${e.message}")
                return@launch
            }
            if (isViewAttached()) {
                getView()?.showMyTeams(teams)
            }
        }
    }

    override fun saveMyTeams(teams: List<String>) {
        firebaseRepository.setDocument(
            collectionPath = "teams",
            data = mapOf("teams" to teams),
            onSuccess = {

            },
            onFailure = { _ ->

            }
        )
    }


}
