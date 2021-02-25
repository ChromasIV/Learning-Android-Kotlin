package com.chromasgaming.sitstandtimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver(private val listener: MyBroadcastListener)  : BroadcastReceiver() {

    private val theListener: MyBroadcastListener = listener

    override fun onReceive(context: Context?, intent: Intent?) {

        //Log.i("Test", "Broadcaster")
        //Toast.makeText(context, "Action: " + intent?.action, Toast.LENGTH_LONG).show()

        Log.i("Time", intent?.extras?.getString("time"))
        intent?.getStringExtra("time")?.let { theListener.doSomething(it) }

        //context?.startService(Intent(context, TimerService::class.java))
    }
}