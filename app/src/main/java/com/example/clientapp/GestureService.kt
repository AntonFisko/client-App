package com.example.clientapp

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import com.example.clientapp.models.GestureCommand
import kotlinx.coroutines.*

//class GestureService(private val context: Context) {
//    fun performGesture(command: GestureCommand) {
//        val service = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityService
//        val path = Path()
//        when (command.type) {
//            "SWIPE_UP" -> {
//                path.moveTo(540f, 1800f)
//                path.lineTo(540f, 300f)
//            }
//            "SWIPE_DOWN" -> {
//                path.moveTo(540f, 300f)
//                path.lineTo(540f, 1800f)
//            }
//            else -> return
//        }
//        service.dispatchGesture(android.accessibilityservice.GestureDescription.Builder()
//            .addStroke(android.accessibilityservice.GestureDescription.StrokeDescription(path, 0, command.duration))
//            .build(), null, null)
//    }
//}
