package com.xiamubobby.kotlinextensions.androidextensions

/**
 * Created by natsuki on 16/3/10.
 */

public fun <T: Comparable<T>> T.minWith(other: T): T {
    if (this.compareTo(other) < 0) {
        return this
    } else {
        return other
    }
}

public fun <T: Comparable<T>> T.maxWith(other: T): T {
    if (this.compareTo(other) > 0) {
        return this
    } else {
        return other
    }
}

public fun <T: Comparable<T>> T.boundInsideWith(min: T, max: T): T {
    if (this.compareTo(max) > 0) {
        return max
    } else if (this.compareTo(min) < 0){
        return min
    } else {
        return this
    }
}