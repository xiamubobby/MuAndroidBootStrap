package com.xiamubobby.kotlinextensions.ankoextionsions

import android.content.Context
import android.content.res.Resources
import android.view.View
import org.jetbrains.anko.AnkoContext

/**
 * Created by natsuki on 16/3/15.
 */
public fun Context.dimen(id: Int, defaultValue: Int): Int {
    return try { resources.getDimensionPixelSize(id) } catch(e: Resources.NotFoundException) { defaultValue }
}
public fun AnkoContext<*>.dimen(id: Int, defaultValue: Int): Int {
    return ctx.dimen(id, defaultValue)
}
public fun View.dimen(id: Int, defaultValue: Int): Int {
    return context.dimen(id, defaultValue)
}
public fun android.support.v4.app.Fragment.dimen(id: Int, defaultValue: Int): Int {
    return context.dimen(id, defaultValue)
}
public fun android.app.Fragment.dimen(id: Int, defaultValue: Int): Int {
    return context.dimen(id, defaultValue)
}