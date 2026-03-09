package com.sleepstory.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SleepTimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Stop playback when sleep timer expires
        val serviceIntent = Intent(context, AudioPlaybackService::class.java).apply {
            action = AudioPlaybackService.ACTION_STOP
        }
        context.startService(serviceIntent)
    }
}