package com.sun.weatherapp.screen.notify

import com.sun.weatherapp.screen.base.BaseContract


interface NotifyContract : BaseContract<NotifyContract.View, NotifyContract.Presenter> {

    interface View : BaseContract.View {
        fun showNotifications(notifications: List<String>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getNotifications()
        fun addNotification(notification: String)
        fun removeNotification(notification: String)
    }
}