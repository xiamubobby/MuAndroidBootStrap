package com.xiamubobby.kotlinextensions

/**
 * Created by natsuki on 16/3/15.
 */
public class Initable<clazz: Any>() {
    public lateinit var instance: clazz
    public var initialed: Boolean =false
    private fun initial(init:()-> clazz) {
        instance = init()
        initialed = true
    }
    public fun get(init:()-> clazz): clazz {
        if (!initialed) initial(init)
        return instance
    }

    private class InitableNotInitedException: Exception() {
        override val message: String?
            get() = "InitableNotInitedException"
    }
}