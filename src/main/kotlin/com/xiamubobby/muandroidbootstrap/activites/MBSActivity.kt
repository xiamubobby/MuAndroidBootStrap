package com.xiamubobby.muandroidbootstrap.activites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jakewharton.rxbinding.view.clicks
import com.trello.rxlifecycle.RxLifecycle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.xiamubobby.muandroidbootstrap.R
import com.xiamubobby.muandroidbootstrap.utils.MBSLogger
import com.xiamubobby.muandroidbootstrap.utils.theMBSLogger
import kotlinx.android.synthetic.main.temp.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.info
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by natsuki on 16/3/1.
 */
open class MBSActivity: RxAppCompatActivity(), AnkoLogger{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theMBSLogger.context = this
        theMBSLogger.snackSpawnee = findViewById(android.R.id.content)
        info("created")
    }

    override fun onStart() {
        super.onStart()
        info("started")
    }

    override fun onResume() {
        super.onResume()
        theMBSLogger.context = this
        theMBSLogger.snackSpawnee = findViewById(android.R.id.content)
        info("resumed")
    }

    override fun onPause() {
        super.onPause()
        info("paused")
    }

    override fun onStop() {
        super.onStop()
        info("stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        info("destroyed")
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(getContentViewWithRoot(layoutInflater.inflate(layoutResID, null, false)))
    }

    override fun setContentView(view: View?) {
        super.setContentView(getContentViewWithRoot(view))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(getContentViewWithRoot(view, params))
    }

    private fun getContentViewWithRoot(v: View?, params: ViewGroup.LayoutParams? = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)): View {
        val rootFl = FrameLayout(this)
        rootFl.isFocusableInTouchMode = true
        rootFl.addView(v)
        v?.layoutParams = params
        return rootFl
    }

    companion object {
        val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
        val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}