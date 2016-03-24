package com.xiamubobby.kotlinextensions.androidextensions

import android.graphics.LinearGradient
import android.graphics.Shader

/**
 * Created by natsuki on 16/3/15.
 */
public fun LinearGradient.of(x0: Int, y0: Int, x1: Int, y1: Int, color1: Int, color2: Int, shader: Shader.TileMode): LinearGradient {
    return LinearGradient(x0.toFloat(), y0.toFloat(), x1.toFloat(), y1.toFloat(), color1, color2, shader)
}