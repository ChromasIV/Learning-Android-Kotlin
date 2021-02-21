package com.chromasgaming.sitstandtimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class TimerService: Service() {

    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()

        Log.i("Started Service","starting..")

        val timer =  object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var min = millisUntilFinished / 1000 / 60
                var sec = (millisUntilFinished / 1000 % 60)
                val mTextField = when(min == 0L) {
                    true -> when(sec < 10) {
                        true -> "00:0$sec"
                        false -> "00:$sec"
                    }
                    false -> "$min:$sec"
                }
                sendNotify(mTextField)

                Intent().also { intent ->
                    intent.action = "com.chromasgaming.sitstandtimer"
                    intent.putExtra("time", mTextField)
                    sendBroadcast(intent)
                }
            }

            override fun onFinish() {
                //mTextField = "done!"
                notificationManager?.cancel(0)
            }
        }

        timer.start()
    }

    private fun sendNotify(text: String) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "101")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        createNotificationChannel()?.notify(0, builder.build())
        //notificationManager?.notify(0, builder.build())
    }

    private fun createNotificationChannel(): NotificationManager? {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("101", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)

            return notificationManager!!
        }

        return null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}