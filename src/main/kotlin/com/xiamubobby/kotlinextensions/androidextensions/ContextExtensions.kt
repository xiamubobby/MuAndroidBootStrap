package com.xiamubobby.kotlinextensions.androidextensions

import android.Manifest
import android.content.Context
import android.support.v4.content.ContextCompat

/**
 * Created by natsuki on 16/3/2.
 */
//public fun Context.checkPermission(permissionStr: String): CheckPermissionResult {
//    val ret = ContextCompat.checkSelfPermission(this, permissionStr)
//    for (p in CheckPermissionResult.values()) {
//        if (p.ordinal == ret) return p
//    }
//    throw Exception("returned int is not one of the CheckPermissionResults")
//}
public fun Context.checkPermission(permissionStr: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionStr) == 0
}