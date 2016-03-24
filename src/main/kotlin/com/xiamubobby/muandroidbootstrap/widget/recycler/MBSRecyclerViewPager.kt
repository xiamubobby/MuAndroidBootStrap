package com.xiamubobby.muandroidbootstrap.widget.recycler

import android.animation.ValueAnimator
import android.app.ActionBar
import android.content.Context
import android.graphics.*
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import com.xiamubobby.kotlinextensions.androidextensions.boundInsideWith
import com.xiamubobby.kotlinextensions.androidextensions.maxWith
import com.xiamubobby.kotlinextensions.androidextensions.minWith
import com.xiamubobby.kotlinextensions.ankoextionsions.dimen
import com.xiamubobby.kotlinextensions.whenNotNull
import com.xiamubobby.muandroidbootstrap.R
import com.xiamubobby.muandroidbootstrap.utils.theMBSLogger
import org.jetbrains.anko.withAlpha

/**
 * Created by xiamubobby on 3/13/16.
 */
class MBSRecyclerViewPager @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0): RecyclerView(context, attrs, defStyle) {

    enum class SnapIntent {
        LEFT, RIGHT, AUTO
    }
    enum class EdgeShadowType {
        NONE, LINEAR, SOLID
    }
    val PAD_PX = 0//10
    val DEFAULT_EDGE_SHADOW_WIDTH_PX = 20

    public val linearLayoutManager: LinearLayoutManager
    private var currentItemOffset = 0
    private var currentItemIndex = 0
    private var currentTotalOffset = 0
    private var snapAnimator: ValueAnimator? = null
    private var lastIndex = 0
    private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            velocityTracker.computeCurrentVelocity(1)
            if (((lastIndex == layoutManager.itemCount - 1) && velocityTracker.xVelocity <= 0) || ((lastIndex == 0) && velocityTracker.xVelocity >= 0)) {
                snap(SnapIntent.AUTO)
            } else {
                snap(if (velocityTracker.xVelocity > 0) SnapIntent.LEFT else SnapIntent.RIGHT)
            }
            return true
        }
    })
    private val velocityTracker: VelocityTracker = VelocityTracker.obtain()
    private val pageSpaceItemDecoration: ItemDecoration? = null
    private var pageFadeShadowDecoration: ItemDecoration? = null
    val shadowWidth: Int
    val p = Paint()//.apply { isDither = true }
    var pageScrollListener: (MBSRecyclerViewPager.(idx: Int, offset: Float, state: Int)->Unit)? = null
    var pageSettledListener: (MBSRecyclerViewPager.(idx: Int)->Unit)? = null
    public var currentPageIndex: Int = 0
    private var mAdapter: Adapter<ViewHolder>? = null

    init {
        shadowWidth = dimen(R.dimen.mbs_rvp_default_shadow_width_dp, DEFAULT_EDGE_SHADOW_WIDTH_PX)
        linearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        layoutManager = linearLayoutManager
        var lastScrollState = SCROLL_STATE_IDLE
        addOnScrollListener(object: OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState) {
                    SCROLL_STATE_IDLE -> {
                        //if (lastScrollState == SCROLL_STATE_SETTLING) pageSettledListener?.invoke(this@MBSRecyclerViewPager, currentItemIndex)
                        lastScrollState = newState
                    }
                    SCROLL_STATE_DRAGGING -> { lastScrollState = newState }
                    SCROLL_STATE_SETTLING -> { lastScrollState = newState }
                }
            }
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                snapAnimator?.cancel()
                currentTotalOffset += dx
                currentItemIndex = currentTotalOffset / width
                currentItemOffset = currentTotalOffset % width
                val currentItemOffsetPercent = currentItemOffset.toFloat() / width
                tabLayout?.setScrollPosition(currentItemIndex, currentItemOffsetPercent, true)
                if (currentItemOffset >= width) currentItemOffset = currentItemOffset - width
                pageScrollListener?.invoke(this@MBSRecyclerViewPager, currentItemIndex, currentItemOffsetPercent, scrollState)
            }
        })
        attrs.whenNotNull {
            val typedArray = context?.obtainStyledAttributes(attrs,R.styleable.MBSRecyclerViewPager)
            val pageSpacing = typedArray?.getDimension(R.styleable.MBSRecyclerViewPager_mbs_rvvp_page_spacing, 0f)
            setPageSpacing(pageSpacing?.toInt() ?: 0)
            val edgeShadowType = typedArray?.getInt(R.styleable.MBSRecyclerViewPager_mbs_rvvp_edge_shadow_type, 0)
            for (type in EdgeShadowType.values()) {
                if (type.ordinal == edgeShadowType) setEdgeShadowType(type)
            }
            typedArray?.recycle()
        }
    }


    private fun snap(snapIntent: SnapIntent) {
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val firstView = linearLayoutManager.findViewByPosition(firstPosition)
        val firstViewScrollX = currentItemOffset
        val snapTargetPosition = when (snapIntent) {
            SnapIntent.LEFT -> { firstPosition.maxWith(0) }
            SnapIntent.RIGHT -> { (firstPosition + 1).minWith(linearLayoutManager.itemCount - 1) }
            SnapIntent.AUTO -> { (if (firstViewScrollX < firstView.width / 2) firstPosition else firstPosition + 1).boundInsideWith(0, linearLayoutManager.itemCount - 1) }
        }
        currentPageIndex = snapTargetPosition
        val snapDistance = currentTotalOffset - snapTargetPosition * width
        val linearSmoothScroller = object : LinearSmoothScroller(context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
                return linearLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateTimeForScrolling(dx: Int): Int {
                return Math.abs(snapDistance / 15)
            }

            override fun onStop() {
                super.onStop()
                if (lastIndex != snapTargetPosition) pageSettledListener?.invoke(this@MBSRecyclerViewPager, currentPageIndex)
            }

        }
        linearSmoothScroller.targetPosition = snapTargetPosition
        linearLayoutManager.startSmoothScroll(linearSmoothScroller)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastIndex = currentItemIndex
                gestureDetector.onTouchEvent(e)
                return super.onTouchEvent(e)
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker.addMovement(e)
                gestureDetector.onTouchEvent(e)
                return super.onTouchEvent(e)
            }
            MotionEvent.ACTION_UP -> {
                if (!gestureDetector.onTouchEvent(e)) {
                    super.onTouchEvent(e)
                    snap(SnapIntent.AUTO)
                    return false
                } else {
                    return false
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!gestureDetector.onTouchEvent(e)) {
                    super.onTouchEvent(e)
                    snap(SnapIntent.AUTO)
                    return false
                } else {
                    return false
                }
            }
        }
        return super.onTouchEvent(e)
    }

    public fun setPageSpacing(spacing: Int = dimen(R.dimen.mbs_rvp_page_spacing, PAD_PX) / 2) {
        addItemDecoration(object: ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: State?) {
                val position = parent?.getChildAdapterPosition(view)
                if (position == NO_POSITION) {
                    return
                }
                val itemCount = state?.itemCount!!
                if (position == 0) {
                    outRect?.set(0, 0, spacing, 0);
                } else if (itemCount > 0 && position == itemCount - 1) {
                    outRect?.set(spacing, 0, 0, 0);
                } else {
                    outRect?.set(spacing, 0, spacing, 0);
                }
            }
        })
    }

    public fun setEdgeShadowType(typeEdge: EdgeShadowType) {
        if (typeEdge != EdgeShadowType.NONE) {
            pageFadeShadowDecoration = FadingShadowItemDecoration(typeEdge)
            addItemDecoration(pageFadeShadowDecoration)
        }
    }

    var tabLayout: TabLayout? = null
    public fun setUpWithTabLayout(tabLayout: TabLayout?) {
        this.tabLayout = tabLayout
        tabLayout?.removeAllTabs()
        adapter.whenNotNull {
            if (this is Adapter) {
                for (i in 0..linearLayoutManager.itemCount - 1) {
                    tabLayout?.addTab(tabLayout.newTab().apply { text = getTitle(i) })
                }
            }
        }
        tabLayout?.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                smoothScrollToPosition(p0?.position!!)
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })
    }

    private inner class FadingShadowItemDecoration(val typeEdge: EdgeShadowType): ItemDecoration() {
        override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: State?) {
            super.onDrawOver(c, parent, state)
            val w = c?.width ?: 0
            val h = c?.height ?: 0
            p.shader = LinearGradient(
                    (w - shadowWidth - currentItemOffset).toFloat(),
                    (h / 2).toFloat(),
                    (w - currentItemOffset).toFloat(),
                    (h / 2).toFloat(),
                    0x000000.withAlpha(0x00),
                    0x000000.withAlpha(0x22),
                    Shader.TileMode.CLAMP)
            p.alpha = if (typeEdge == EdgeShadowType.LINEAR) (currentItemOffset.toFloat() / width.toFloat() * 255).toInt() else 255
            c?.drawRect(Rect(w - shadowWidth - currentItemOffset, 0, w - currentItemOffset, h), p)
            p.alpha = 255
        }


    }

    public abstract class Adapter<VH: ViewHolder>: RecyclerView.Adapter<VH>() {
        abstract fun getTitle(position: Int): String?
    }

//    companion object {
//        @JvmStatic private val DEFAULT_SNAP_SPEED = 25
//    }
}