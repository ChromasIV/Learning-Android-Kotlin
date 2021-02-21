package com.chromasgaming.sitstandtimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            Log.i("testing", intent.extras?.getString("time"))
        }
        //context?.startService(Intent(context, TimerService::class.java))
        //context!!.startForegroundService(Intent(context, TimerService::class.java))
    }
}