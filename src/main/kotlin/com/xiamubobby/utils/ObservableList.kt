package com.xiamubobby.utils

import com.xiamubobby.muandroidbootstrap.utils.theMBSLogger

/**
 * Created by xiamubobby on 3/13/16.
 */
class ObservableList<E>(private val theList: MutableList<E>, private val callbacks: ListObservableCallbacks<E>): MutableList<E> by theList {
    override fun add(element: E): Boolean {
        callbacks.add(element)
        return theList.add(element)
    }

    override fun remove(element: E): Boolean {
        callbacks.remove(element)
        return theList.remove(element)
    }

    public abstract class ListObservableCallbacks<E> {
        abstract fun add(element: E)
        fun remove(element: E) {}
    }
}