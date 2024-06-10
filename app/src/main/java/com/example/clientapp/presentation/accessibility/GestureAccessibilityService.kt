package com.example.clientapp.presentation.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val type = intent?.getStringExtra("GESTURE_COMMAND_TYPE")
        val duration = intent?.getLongExtra("GESTURE_COMMAND_DURATION", 1000L) ?: 1000L

        if (type != null) {
            performSwipeGesture(type, duration)
        }
        return START_STICKY
    }

    private fun performSwipeGesture(type: String, duration: Long) {
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
                Log.d("GestureService", "Жест $type выполнен")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d("GestureService", "Жест $type отменен")
            }
        }, null)
    }
}
