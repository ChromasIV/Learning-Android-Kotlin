package com.chromasgaming.sitstandtimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity(), MyBroadcastListener {

    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val receiver: BroadcastReceiver = MyReceiver(this)

        val filter = IntentFilter().apply {
            addAction("com.chromasgaming.sitstandtimer.TEST")
        }
        registerReceiver(receiver, filter)

       val mTextField: TextView = findViewById(R.id.text1)

       val timer =  object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var min = millisUntilFinished / 1000 / 60
                var sec = (millisUntilFinished / 1000 % 60)
                mTextField.text = when(min == 0L) {
                    true -> "00:$sec"
                    false -> "$min:$sec"
                }
                sendNotify(mTextField.text as String)
            }

            override fun onFinish() {
                mTextField.text = "done!"
            }
        }
        val mButton: Button = findViewById(R.id.button)
        val mButton2: Button = findViewById(R.id.button2)


        mButton.setOnClickListener {
            startService(Intent(this, TimerService::class.java))
            //sendBroadcast(Intent("com.chromasgaming.sitstandtimer.TEST"))


//            Intent().also { intent ->
//                intent.action = "com.chromasgaming.sitstandtimer.TEST"
//                sendBroadcast(intent)
//                Log.i("Test", "sennt")
//            }
            //sendBroadcast(Intent(this, TimerService::class.java))
//            runBlocking {
//                val job = launch {
//                    timer.start()
//                    sendNotify("Start")
//                    println("${Thread.currentThread()} has run.")
//                }
//            }

         }

        mButton2.setOnClickListener {

        }
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

    override fun doSomething(value: String) {
        val mTextField: TextView = findViewById(R.id.text1)
        mTextField.text = value
    }
}