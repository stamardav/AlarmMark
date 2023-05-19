package com.example.alarmmark

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    private var alarmNotificationManager: NotificationManager? = null
    var NOTIFICATION_CHANNEL_ID = "null"
    var NOTIFICATION_CHANNEL_NAME = "null"
    private val NOTIFICATION_ID = 1


    // выполнение действий
    override fun onReceive(context: Context, intent: Intent) {
        playAudio(context)
        sendNotification(context)
    }

    // аудио
    private fun playAudio(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        mediaPlayer.start()
    }


    // уведомление
    private fun sendNotification(context: Context) {
        val notifTitle = "Будильник!"
        val notifContent = "Ваш будильник прозвенел"
        alarmNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val newIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            newIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance
            )
            alarmNotificationManager!!.createNotificationChannel(mChannel)
        }

        val inboxStyle = NotificationCompat.BigTextStyle().bigText(notifContent)
        val notifBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        notifBuilder.setContentTitle(notifTitle)
        notifBuilder.setSmallIcon(android.R.drawable.ic_popup_reminder)
        notifBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        notifBuilder.setContentText(notifContent)
        notifBuilder.setAutoCancel(true)
        notifBuilder.setStyle(inboxStyle)
        notifBuilder.setContentIntent(contentIntent)
        alarmNotificationManager!!.notify(NOTIFICATION_ID, notifBuilder.build())
    }
}