package com.example.clientapp


import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class GestureAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Не используем
    }

    override fun onInterrupt() {
        // Не используем
    }

    fun performSwipeUp(duration: Long) {
        val path = Path().apply {
            moveTo(500f, 1500f)
            lineTo(500f, 500f)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        dispatchGesture(gesture, null, null)
    }

    fun performSwipeDown(duration: Long) {
        val path = Path().apply {
            moveTo(500f, 500f)
            lineTo(500f, 1500f)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        dispatchGesture(gesture, null, null)
    }
}
