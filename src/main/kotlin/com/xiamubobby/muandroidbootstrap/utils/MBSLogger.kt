package com.xiamubobby.muandroidbootstrap.utils

import android.Manifest
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import com.xiamubobby.kotlinextensions.androidextensions.checkPermission
import com.xiamubobby.kotlinextensions.androidextensions.snack
import com.xiamubobby.kotlinextensions.whenNotNull
import com.xiamubobby.kotlinextensions.whenTrue
import com.xiamubobby.muandroidbootstrap.R
import com.xiamubobby.muandroidbootstrap.activites.MBSActivity
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.notificationManager
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by natsuki on 16/3/2.
 */
abstract class MBSLogger: AnkoLogger {
    constructor(ctx: MBSActivity?) {
        context = ctx
    }
    enum class LogTarget(val flag: Int) {
        LOGCAT
                (1 shl 0),
        FILE
                (1 shl 1),
        NOTIFICATION
                (1 shl 2),
        SNACKBAR
                (1 shl 3),
        TOAST
                (1 shl 4),
        DIALOG
                (1 shl 5)
    }
    private var logTargetFlag: Int = LogTarget.LOGCAT.flag

    public fun setTargetFlags(vararg logTargets: LogTarget) {
        var ret: Int = LogTarget.LOGCAT.flag
        for (t in logTargets) {
            ret = ret or t.flag
        }
        logTargetFlag = ret
    }
    public fun addTargetFlags(vararg logTargets: LogTarget) {
        for (t in logTargets) {
            logTargetFlag = logTargetFlag or t.flag
        }
    }
    public fun removeTargetFlags(vararg logTargets: LogTarget) {
        for (t in logTargets) {
            if (t == LogTarget.LOGCAT) continue
            logTargetFlag = logTargetFlag and (t.flag.inv())
        }
    }
    abstract var context: MBSActivity?
    abstract var snackSpawnee: View?

    public fun resolveLoggerTag(logger: Any): String {
        val tag = logger.javaClass.simpleName
        return if (tag.length <= 23) {
            tag
        } else {
            tag.substring(0, 23)
        }
    }

    public fun debug(loggee: Any?, tag: String = loggerTag) {
        info(loggee.toString(), tag)
    }
    public fun debug(logText: String, tag: String) {
        if (logTargetFlag and LogTarget.LOGCAT.flag != 0) {
            Log.d(tag, logText)
        }
        if (logTargetFlag and LogTarget.FILE.flag != 0) {

        }
        if (logTargetFlag and LogTarget.NOTIFICATION.flag != 0) {
            log2Notification(logText)
        }
        if (logTargetFlag and LogTarget.SNACKBAR.flag !=0) {
            snackSpawnee?.snack(logText)
        }
    }

    public fun error(loggee: Any?, tag: String = loggerTag) {
        error(loggee.toString(), tag)
    }
    public fun error(logText: String, tag: String) {
        if (logTargetFlag and LogTarget.LOGCAT.flag != 0) {
            Log.e(tag, logText)
        }
        if (logTargetFlag and LogTarget.FILE.flag != 0) {

        }
        if (logTargetFlag and LogTarget.NOTIFICATION.flag != 0) {
            log2Notification(logText)
        }
        if (logTargetFlag and LogTarget.SNACKBAR.flag !=0) {
            snackSpawnee?.snack(logText)
        }
    }

    public fun info(loggee: Any?, tag: String = loggerTag) {
        info(loggee.toString(), tag)
    }
    public fun info(logText: String, tag: String) {
        if (logTargetFlag and LogTarget.LOGCAT.flag != 0) {
            Log.i(tag, logText)
        }
        if (logTargetFlag and LogTarget.FILE.flag != 0) {

        }
        if (logTargetFlag and LogTarget.NOTIFICATION.flag != 0) {
            log2Notification(logText)
        }
        if (logTargetFlag and LogTarget.SNACKBAR.flag !=0) {
            snackSpawnee?.snack(logText)
        }
    }

    public fun intAccessibilityEventToString(id: Int): String {
        var ret: String = "Unfound Event Id"
        when(id) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> { ret = "TYPE_VIEW_CLICKED "}
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> { ret = "TYPE_VIEW_LONG_CLICKED "}
            AccessibilityEvent.TYPE_VIEW_SELECTED -> { ret = "TYPE_VIEW_SELECTED "}
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> { ret = "TYPE_VIEW_FOCUSED "}
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> { ret = "TYPE_VIEW_TEXT_CHANGED "}
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> { ret = "TYPE_WINDOW_STATE_CHANGED "}
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> { ret = "TYPE_NOTIFICATION_STATE_CHANGED "}
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> { ret = "TYPE_VIEW_HOVER_ENTER "}
            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> { ret = "TYPE_VIEW_HOVER_EXIT "}
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START -> { ret = "TYPE_TOUCH_EXPLORATION_GESTURE_START "}
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> { ret = "TYPE_TOUCH_EXPLORATION_GESTURE_END "}
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> { ret = "TYPEWINDOW_CONTENT_CHANGED" }
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> { ret = "TYPE_VIEW_SCROLLED"}
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> { ret = "TYPE_VIEW_TEXT_SELECTION_CHANGED" }
            AccessibilityEvent.TYPE_ANNOUNCEMENT -> { ret = "TYPE_ANNOUNCEMENT" }
            AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> { ret = "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY" }
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> { ret = "TYPE_GESTURE_DETECTION_START" }
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> { ret = "TYPE_GESTURE_DETECTION_END" }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> { ret = "TYPE_TOUCH_INTERACTION_START" }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> { ret = "TYPE_TOUCH_INTERACTION_END" }
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> { ret = "TYPE_WINDOWS_CHANGED" }
            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED -> { ret = "TYPE_VIEW_CONTEXT_CLICKED" }
        }
        return ret
    }

    private var timeStamp = System.currentTimeMillis()
    public fun tic() {
        timeStamp = System.currentTimeMillis()
    }

    public fun toc() {
        info(System.currentTimeMillis() - timeStamp)
    }

    private var latestLogStrings: MutableList<String> = arrayListOf("")
    private val notificationId: Int = Random().nextInt()
    private fun log2Notification(logText: String) {
        latestLogStrings.add(0, logText)
        context.whenNotNull {
            val notificationBuilder = NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.style_logos_product_grid_geometry2)
                    .setContentTitle("MBSLogger")
            val notificationStyle = android.support.v4.app.NotificationCompat.BigTextStyle()
            notificationBuilder.setStyle(notificationStyle)
            notificationStyle.bigText(latestLogStrings.joinToString("\n"))
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    private fun log2File(logText: String) {
        context.whenNotNull {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }
        }
    }
}