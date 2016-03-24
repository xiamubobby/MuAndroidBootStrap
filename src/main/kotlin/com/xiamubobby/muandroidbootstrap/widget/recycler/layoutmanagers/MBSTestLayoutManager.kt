package com.xiamubobby.muandroidbootstrap.widget.recycler.layoutmanagers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by xiamubobby on 3/16/16.
 */
class MBSTestLayoutManager: RecyclerView.LayoutManager() {
    private val offScreenPageCapacity: Int = 1
    private var pageViews: Array<View?> = arrayOf(null, null, null)
    public var currentPage: Int = 1
    public var currentTotalOffset = 0

    //public var linearSmoothScroller = LinearSmootahScroller()


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams? {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        val count = itemCount
        for (i in 0.. 2 * offScreenPageCapacity) {
            val page = i + currentPage - offScreenPageCapacity
            if (page in 0..count) {
                if (pageViews[i] != null) removeAndRecycleView(pageViews[i], recycler)
                pageViews[i] = recycler?.getViewForPosition(page)
                if (pageViews[i] != null) {
                    measureChildWithMargins(pageViews[i], 0, 0)
                    addView(pageViews[i])
                    layoutDecorated(pageViews[i], i * width, 0, (i + 1) * width, height)
                }
            }
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        currentTotalOffset += dx
        offsetChildrenHorizontal(-dx)
        return dx
    }

}