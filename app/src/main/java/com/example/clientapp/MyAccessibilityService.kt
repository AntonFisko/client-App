package com.example.clientapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.example.clientapp.models.GestureCommand
import com.example.clientapp.models.GestureResult

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log

class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if necessary
    }

    override fun onInterrupt() {
        // Handle interrupt
    }

    fun performSwipeGesture(type: String, duration: Long) {
        val path = Path()
        when (type) {
            "SWIPE_UP" -> {
                path.moveTo(500f, 1500f)
                path.lineTo(500f, 500f)
            }
            "SWIPE_DOWN" -> {
                path.moveTo(500f, 500f)
                path.lineTo(500f, 1500f)
            }
            else -> {
                Log.e("MyAccessibilityService", "Unknown gesture type: $type")
                return
            }
        }

        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, duration))
        val gesture = gestureBuilder.build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d("MyAccessibilityService", "Gesture completed")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d("MyAccessibilityService", "Gesture cancelled")
            }
        }, null)
    }
}