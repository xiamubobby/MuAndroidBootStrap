package com.xiamubobby.muandroidbootstrap.widget

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import com.xiamubobby.kotlinextensions.androidextensions.items
import com.xiamubobby.kotlinextensions.whenNotNull
import com.xiamubobby.muandroidbootstrap.BuildConfig

/**
 * Created by natsuki on 16/3/9.
 */
class RecyclerLayeredCell: View {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mainLayout: View? = null
    private var backLayout: View? = null

    public fun inflateMenuRes(menuResId: Int) {
        val activity = context
        if (activity is Activity) {
            val menu: Menu? = null
            activity.menuInflater.inflate(menuResId, menu)
            menu.whenNotNull {
                for (any in items) {
                    Log.i("1tems", any.toString())
                }
            }
        }
    }
}