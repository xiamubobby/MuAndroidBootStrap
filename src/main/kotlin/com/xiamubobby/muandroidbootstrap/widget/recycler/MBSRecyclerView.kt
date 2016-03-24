package com.xiamubobby.muandroidbootstrap.widget.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.xiamubobby.kotlinextensions.ankoextionsions.dimen
import com.xiamubobby.muandroidbootstrap.R
import org.jetbrains.anko.dip

/**
 * Created by natsuki on 16/3/11.
 */
class MBSRecyclerView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0): RecyclerView(context, attrs, defStyle) {

    var dc: Int? = null
    var dw: Int? = null
    var did: MBSRecyclerViewDividerDecoration? = null

    companion object {
        val NO_DIVIDER = -1
        @JvmStatic val DEFAULT_DIVIDER_WIDTH_PX = 48
        @JvmStatic val DEFAULT_DIVIDER_WIDTH_DP_RES = R.dimen.mbs_rv_dafault_divider_dp
    }

    init {
        val typedArray = context?.obtainStyledAttributes(attrs,R.styleable.MBSRecyclerView)
        if (typedArray != null) {
            dc = typedArray.getColor(R.styleable.MBSRecyclerView_mbs_rv_divider_color, NO_DIVIDER)
            dw = typedArray.getDimensionPixelSize(R.styleable.MBSRecyclerView_mbs_rv_divider_width, NO_DIVIDER)
            if (dc == NO_DIVIDER && dw != NO_DIVIDER) {
                dc = ContextCompat.getColor(context, R.color.mbs_rv_default_divider_color)
            }
            if (dw == NO_DIVIDER && dc != NO_DIVIDER) {
                dw == context?.dimen(DEFAULT_DIVIDER_WIDTH_DP_RES, DEFAULT_DIVIDER_WIDTH_PX) ?: DEFAULT_DIVIDER_WIDTH_PX
            }
            if (dw != NO_DIVIDER && dc != NO_DIVIDER) {
                did = MBSRecyclerViewDividerDecoration(dc!!, dw!!)
                addItemDecoration(did)
            }
        }
        typedArray?.recycle()
    }

    enum class ViewType {STATIC_CONTENT, SUB_DATA_SET}

    private var realTotalSize: Int = 0

    public fun addStaticContent() {

    }

    public inner class MBSRecyclerAdapter<VH: ViewHolder>: Adapter<VH>() {
        override fun onBindViewHolder(p0: VH, p1: Int) {
            throw UnsupportedOperationException()
        }

        override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): VH {
            throw UnsupportedOperationException()
        }

        override fun getItemCount(): Int {
            return realTotalSize
        }
    }

    public abstract class MBSRecyclerStaticContentAdapter<VH: ViewHolder>(val capacity: Int = 1): Adapter<VH>() {
        override abstract fun onCreateViewHolder(p0: ViewGroup?, p1: Int): VH

        override abstract fun onBindViewHolder(p0: VH, p1: Int)

        override fun getItemCount(): Int {
            return capacity
        }
    }

    public inner class MBSRecyclerViewDividerDecoration(val dividerColor: Int, val dividerWidth: Int): ItemDecoration() {

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: State?) {
            val llm = layoutManager as? LinearLayoutManager
            if (llm != null) {
                val o = llm.orientation
                val pos = parent?.getChildAdapterPosition(view)
                if (pos == RecyclerView.NO_POSITION) {
                    return
                }
                if (o == LinearLayoutManager.HORIZONTAL) {
                    val itemCount = state?.itemCount!!
                    if (itemCount > 1 && pos == itemCount - 1) {
                        outRect?.set(0, 0, 0, 0)
                    } else {
                        outRect?.set(0, 0, dividerWidth, 0)
                    }
                } else if(o == LinearLayoutManager.VERTICAL) {
                    val itemCount = state?.itemCount!!
                    if (itemCount > 1 && pos == itemCount - 1) {
                        outRect?.set(0, 0, 0, 0)
                    } else {
                        outRect?.set(0, 0, 0, dividerWidth)
                    }
                }
            }
        }

        private val pt = Paint()
        override fun onDraw(c: Canvas?, parent: RecyclerView?, state: State?) {
            val left = parent?.paddingLeft ?: 0
            val right = (parent?.width ?: 0) - (parent?.paddingRight ?: 0)
            val childCount = parent?.childCount ?: 0
            if (childCount > 1) {
                for (i in 0..childCount - 2) {
                    val child = parent?.getChildAt(i)
                    val p = child?.layoutParams as? RecyclerView.LayoutParams
                    val top = child?.bottom!! + p?.bottomMargin!!
                    val bottom = top + dividerWidth
                    pt.color = dividerColor
                    c?.drawRect(Rect(left, top, right, bottom), pt)

                }
            }

        }

    }
}