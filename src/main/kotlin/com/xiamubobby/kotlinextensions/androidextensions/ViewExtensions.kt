package com.xiamubobby.kotlinextensions.androidextensions

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Rect
import android.support.design.widget.Snackbar
import android.support.v4.view.GestureDetectorCompat
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.xiamubobby.kotlinextensions.whenNotNull
import com.xiamubobby.muandroidbootstrap.utils.theMBSLogger
import org.jetbrains.anko.dip
import org.jetbrains.anko.onFocusChange

/**
 * Created by natsuki on 16/3/9.
 */

public @JvmOverloads fun View.layeredWith(backView: View, scrollRatio: Float = 1f): View {
    var _parent = parent
    _parent.whenNotNull {
        if (_parent is ViewGroup) {
            (_parent as ViewGroup).removeView(this@layeredWith)
        }
    }
    val retFl = FrameLayout(context)
    retFl.layoutParams = this.layoutParams
    retFl.addView(backView)
    if (backView.layoutParams == null) {
        backView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT)
    }
    backView.translationX = backView.width * scrollRatio
    retFl.addView(this)
    this.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    if (_parent is ViewGroup) {
        (_parent as ViewGroup).addView(retFl)
    }

    var downX = 0f
    var latestX = 0f
    var deltaX = 0f
    val SCROLL_THREASHOLD_HORIZONTAL = dip(10)
    var enteredScrollProc = false
    val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (velocityX < 0) {
                ValueAnimator.ofFloat(translationX, -backView.width.toFloat()).apply {
                    duration = ((translationX - (-backView.width.toFloat())) / 400 * 1000).toLong()
                    interpolator = DecelerateInterpolator()
                    addUpdateListener {translationX = (it.animatedValue as Float); backView.translationX = backView.width * scrollRatio + translationX * scrollRatio}
                }.start()
            } else {
                ValueAnimator.ofFloat(translationX, 0f).apply {
                    duration = ((0 - translationX) / 400 * 1000).toLong()
                    interpolator = DecelerateInterpolator()
                    addUpdateListener {translationX = (it.animatedValue as Float); backView.translationX = backView.width * scrollRatio + translationX * scrollRatio}
                }.start()
            }
            return true
        }
    })
    fun snapToEdge() {
        if (translationX < -backView.width / 2) {
            ValueAnimator.ofFloat(translationX, -backView.width.toFloat()).apply {
                duration = ((translationX - (-backView.width.toFloat())) / 400 * 1000).toLong()
                interpolator = DecelerateInterpolator()
                addUpdateListener {translationX = (it.animatedValue as Float); backView.translationX = backView.width * scrollRatio + translationX * scrollRatio}
            }.start()
        } else {
            ValueAnimator.ofFloat(translationX, 0f).apply {
                duration = ((0 - translationX) / 400 * 1000).toLong()
                interpolator = DecelerateInterpolator()
                addUpdateListener {translationX = (it.animatedValue as Float); backView.translationX = backView.width * scrollRatio + translationX * scrollRatio}
            }.start()
        }
    }
    this.setOnTouchListener { view, motionEvent ->
        //gestureDetector.onTouchEvent(motionEvent)
        when(motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                gestureDetector.onTouchEvent(motionEvent)
                enteredScrollProc = false
                downX = motionEvent.x
                return@setOnTouchListener true
            }
            MotionEvent.ACTION_MOVE -> {
                gestureDetector.onTouchEvent(motionEvent)
                latestX = motionEvent.x
                deltaX = latestX - downX
                theMBSLogger.info(deltaX)
                if (!enteredScrollProc && Math.abs(deltaX) > SCROLL_THREASHOLD_HORIZONTAL) {
                    enteredScrollProc = true
                }
                if (!enteredScrollProc) {
                    return@setOnTouchListener  false
                } else {
                    _parent.requestDisallowInterceptTouchEvent(true)
                    val newTranslationX = (translationX + deltaX).minWith(0f).maxWith(-backView.width.toFloat())
                    translationX = newTranslationX
                    backView.translationX = backView.width * scrollRatio + translationX * scrollRatio
                    return@setOnTouchListener true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!gestureDetector.onTouchEvent(motionEvent)) {
                    snapToEdge()
                }
                enteredScrollProc = false
                _parent.requestDisallowInterceptTouchEvent(false)
                return@setOnTouchListener false
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!gestureDetector.onTouchEvent(motionEvent)) {
                    snapToEdge()
                }
                enteredScrollProc = false
                _parent.requestDisallowInterceptTouchEvent(false)
                return@setOnTouchListener false
            }
        }
        _parent.requestDisallowInterceptTouchEvent(false)
        return@setOnTouchListener false
    }
    val c = context
    if (c is Activity) {
        val v = c.findViewById(android.R.id.content)
        retFl.touchDelegate = TouchDelegate(Rect(0, 0, v.width, v.height), retFl)
    }
//    retFl.isFocusableInTouchMode = true
//    retFl.onFocusChange { view, b ->
//        if(!b) {
//            ValueAnimator.ofFloat(translationX, 0f).apply {
//                duration = ((translationX - (-backView.width.toFloat())) / 400 * 1000).toLong()
//                interpolator = DecelerateInterpolator()
//                addUpdateListener { translationX = (it.animatedValue as Float); backView.translationX = backView.width * scrollRatio + translationX * scrollRatio }
//            }.start()
//        }
//    }
    return retFl
}

public @JvmOverloads fun View.snack(context: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, context, duration).show()
}
