package com.example.clientapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class GestureAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) {
        // No-op
    }

    override fun onInterrupt() {
        // No-op
    }

    fun performSwipeGesture(type: String, duration: Long) {
        val path = Path().apply {
            when (type) {
                "SWIPE_UP" -> {
                    moveTo(500f, 1000f)
                    lineTo(500f, 100f)
                }
                "SWIPE_DOWN" -> {
                    moveTo(500f, 100f)
                    lineTo(500f, 1000f)
                }
            }
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d("GestureService", "Gesture $type completed")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d("GestureService", "Gesture $type cancelled")
            }
        }, null)
    }
}
