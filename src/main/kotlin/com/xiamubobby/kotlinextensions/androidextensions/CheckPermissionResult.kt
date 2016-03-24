package com.xiamubobby.kotlinextensions.androidextensions

import android.content.pm.PackageManager

/**
 * Created by natsuki on 16/3/2.
 */
enum class CheckPermissionResult(value: Int) {
    PERMISSION_GRANTED(PackageManager.PERMISSION_GRANTED),
    PERMISSION_DENIED(PackageManager.PERMISSION_DENIED)
}