package com.example.clientapp.presentation.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Обработка событий доступности при необходимости
    }

    override fun onInterrupt() {
        // Обработка прерываний
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
                Log.e("MyAccessibilityService", "Неизвестный тип жеста: $type")
                return
            }
        }

        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, duration))
        val gesture = gestureBuilder.build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d("MyAccessibilityService", "Жест выполнен")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d("MyAccessibilityService", "Жест отменен")
            }
        }, null)
    }
}
