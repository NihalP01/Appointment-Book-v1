package com.example.medomind.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.medomind.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {


    private val notificationChannelId: String = "MainNotificationChannel"
    private val pendingIntentRequest: Int = 1001


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            setupChannel(notificationManager)

        notificationManager.notify(1000000, createNotification(p0))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChannel(notificationManager: NotificationManagerCompat) {
        val adminChannel = NotificationChannel(
            notificationChannelId,
            "MAIN_CHANNEL",
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = "This is main channel for notification"
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager.createNotificationChannel(adminChannel)
    }

    private fun createNotification(message: RemoteMessage): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setAutoCancel(true)
            .setVibrate(createVibration())
            .setLights(Color.RED, 1, 1)
            .setSound(defaultSoundUri())
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.notification?.body))
        //.setContentIntent(createIntent(message))
        return notificationBuilder.build()
    }


    private fun defaultSoundUri(): Uri? {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }

    private fun createVibration(): LongArray? {
        return longArrayOf(0, 1000, 0)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


}