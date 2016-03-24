package com.xiamubobby.kotlinextensions

import com.xiamubobby.muandroidbootstrap.utils.MBSLogger
import com.xiamubobby.muandroidbootstrap.utils.theMBSLogger

/**
 * Created by xiamubobby on 3/23/16.
 */
val Any.loggerTag: String
get() = theMBSLogger.resolveLoggerTag(this)

val Any.logger: MBSLogger
get() = theMBSLogger

fun Any.info(loggee: Any) = logger.info(loggee, loggerTag)
fun Any._info_(loggee: Any) = logger.info(loggee, loggerTag)
fun Any.logInfo(loggee: Any) = logger.info(loggee, loggerTag)
