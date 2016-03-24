package com.xiamubobby.kotlinextensions

import com.xiamubobby.utils.ObservableList

/**
 * Created by xiamubobby on 3/13/16.
 */
public fun <E> MutableList<E>.observeWith(callbacks: ObservableList.ListObservableCallbacks<E>): ObservableList<E> {
    return ObservableList(this, callbacks)
}