package com.xiamubobby.muandroidbootstrap.widget.viewpager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import com.xiamubobby.kotlinextensions.observeWith
import com.xiamubobby.utils.ObservableList

/**
 * Created by xiamubobby on 3/13/16.
 */
class MBSViewPager: ViewPager {
    constructor(context: Context?) : super(context) { init(context) }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(context) }

    fun init(ctx: Context?) {
    }

    val pageList = mutableListOf<Page>().observeWith(object: ObservableList.ListObservableCallbacks<Page>() {
        override fun add(element: Page) {
            post{adapter?.notifyDataSetChanged()}
        }

    })

    public fun addPage(page: Page) {
        addPages(page)
    }

    public fun addPages(vararg pages: Page) {
        for (page in pages) {
            pageList.add(page)
        }
    }

    public interface Page {
        public val fragment: Fragment
        public val title: String
    }

    public inner class MBSViewPagerAdapter: FragmentStatePagerAdapter((context as? AppCompatActivity)?.supportFragmentManager) {
        override fun getItem(p0: Int): Fragment? {
            return pageList[p0].fragment
        }

        override fun getCount(): Int {
            return pageList.size
        }

    }
}