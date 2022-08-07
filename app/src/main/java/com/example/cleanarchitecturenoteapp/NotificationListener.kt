package com.example.cleanarchitecturenoteapp

import android.app.Notification
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import android.util.Log
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*

data class Notif(val inum: Int) {
    var message: String = ""
    var title: String = ""
    var packageName: String = ""
    var time: String = ""
}

class NotificationListener : NotificationListenerService() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras: Bundle = sbn.getNotification().extras
        if (sbn.getNotification().flags and Notification.FLAG_GROUP_SUMMARY !== 0) {
            Log.d("error", "Ignore the notification FLAG_GROUP_SUMMARY")
            return
        }
        var title = ""
        var text = ""
        try {
            if (extras.get("android.title") is String) {
                title = extras.getString("android.title").toString()
            }
            if (extras.get("android.title") is SpannableString) {
                title = extras.get("android.title").toString()
            }
            text = extras.getCharSequence("android.text").toString()
        } catch (e: Exception) {
            Log.i("exception", e.message.toString())
        }
        val date: String =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val notif = Notif(sbn.getId())
        notif.message = text
        notif.title = title
        notif.time = date
        notif.packageName =sbn.getPackageName()

        Log.i("MY NOTE", notif.toString())

        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}