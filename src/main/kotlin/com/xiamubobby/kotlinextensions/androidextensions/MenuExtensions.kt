package com.xiamubobby.kotlinextensions.androidextensions

import android.view.Menu
import java.util.*

/**
 * Created by natsuki on 16/3/9.
 */
public val Menu.items: List<Any>
    get() {
        val size = size()
        val list: MutableList<Any>  = ArrayList(size)
        for (i in 0..(size-1)) {
            list[i] = getItem(i)
        }
        return list
    }