package com.sun.weatherapp.screen.notify

import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.data.helper.PreferenceHelper
import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.delay

class NotifyPresenter : BasePresenter<NotifyContract.View>(), NotifyContract.Presenter {

    private var pref: PreferenceHelper = WeatherApplication.getInstance().preferenceHelper

    override fun getNotifications() {
        val list = pref.getStringList(PreferenceHelper.KEY_NOTIFICATIONS, emptyList())
        getView()?.showNotifications(list)
    }

    override fun addNotification(notification: String) {
        val currentNotifications = pref.getStringList(PreferenceHelper.KEY_NOTIFICATIONS, emptyList())
        val updatedNotifications = currentNotifications.toMutableSet().apply {
            add(notification)
        }
        pref.putStringList(PreferenceHelper.KEY_NOTIFICATIONS, updatedNotifications.toList())
        getView()?.showNotifications(updatedNotifications.toList())
    }

    override fun removeNotification(notification: String) {
        val currentNotifications = pref.getStringList(PreferenceHelper.KEY_NOTIFICATIONS, emptyList())
        val updatedNotifications = currentNotifications.toMutableSet().apply {
            remove(notification)
        }
        pref.putStringList(PreferenceHelper.KEY_NOTIFICATIONS, updatedNotifications.toList())
        getView()?.showNotifications(updatedNotifications.toList())
    }

}