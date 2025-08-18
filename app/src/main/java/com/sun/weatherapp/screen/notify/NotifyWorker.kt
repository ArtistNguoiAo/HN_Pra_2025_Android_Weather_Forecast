package com.sun.weatherapp.screen.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.resumeWithException

class NotifyWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    val locationRepository =  LocationRepository.getInstance(
        LocationLocalDataSource.getInstance(WeatherApplication.getInstance().locationService)
    )
    val weatherRepository= WeatherRepository.getInstance(
        WeatherRemoteDataSource.getInstance(),
        WeatherLocalDataSource.getInstance()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun doWork(): Result = kotlinx.coroutines.coroutineScope {
        try {

            val location = kotlinx.coroutines.suspendCancellableCoroutine { cont ->
                locationRepository.getCurrentLocation(object : OnResultListener<Location> {
                    override fun onSuccess(data: Location) = cont.resume(data) {}
                    override fun onError(exception: Exception?) = cont.resumeWithException(exception ?: Exception("Unknown"))
                })
            }

            val weather = kotlinx.coroutines.suspendCancellableCoroutine { cont ->
                weatherRepository.getCurrentWeather(location.latitude, location.longitude,
                    object : OnResultListener<WeatherResponse> {
                        override fun onSuccess(data: WeatherResponse) = cont.resume(data) {}
                        override fun onError(exception: Exception?) = cont.resumeWithException(exception ?: Exception("Unknown"))
                    })
            }

            val info = weather.weather[0].description
            showNotification("Thời tiết", info)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            showNotification("Lỗi", e.message ?: "Có lỗi xảy ra")
            Result.failure()
        }
    }

    private fun showNotification(title: String, content: String) {
        val channelId = "notify_channel"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notify Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
