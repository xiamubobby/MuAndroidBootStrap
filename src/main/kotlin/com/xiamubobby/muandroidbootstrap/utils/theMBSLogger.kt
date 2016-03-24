package com.xiamubobby.muandroidbootstrap.utils

import android.view.View
import com.xiamubobby.muandroidbootstrap.activites.MBSActivity
import kotlin.properties.Delegates

/**
 * Created by natsuki on 16/3/10.
 */
object theMBSLogger : MBSLogger(null) {
    init {
            addTargetFlags(LogTarget.LOGCAT, LogTarget.NOTIFICATION, LogTarget.SNACKBAR)
    }
    override var context: MBSActivity? = null
    override var snackSpawnee: View? = null
}

val Any.logger: MBSLogger
    get() = theMBSLogger

fun Any.info(logText: String) { logger.info(logText) }
fun Any.logInfo(logText: String) { logger.info(logText) }
fun Any._info_(logText: String) { logger.info(logText) }