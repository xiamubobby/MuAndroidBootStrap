package com.xiamubobby.kotlinextensions

/**
 * Created by natsuki on 16/3/2.
 */
public inline fun <T> T?.whenNotNull(todo: T.() -> Unit) {
    if (this != null) {
        this.todo()
    }
}

public inline fun Boolean.whenTrue(todo: Boolean.() -> Unit) {
    if (this) {
        todo()
    }
}