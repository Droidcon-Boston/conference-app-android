package com.mentalmachines.droidconboston.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State

class DividerItemDecoration(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {

    private val divider: Drawable?

    private var orientationValue: Int = 0

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
    }

    private fun drawHorizontal(c: Canvas?, parent: RecyclerView?) {
        val top = parent!!.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            @Suppress("DEPRECATION") val left =
                child.right + params.rightMargin + Math.round(ViewCompat.getTranslationX(child))
            val right = left + divider!!.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c!!)
        }
    }

    private fun drawVertical(c: Canvas?, parent: RecyclerView?) {
        val left = parent!!.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            @Suppress("DEPRECATION") val top =
                child.bottom + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child))
            val bottom = top + divider!!.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c!!)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        if (orientationValue == VERTICAL_LIST) {
            outRect.set(0, 0, 0, divider!!.intrinsicHeight)
        } else {
            outRect.set(0, 0, divider!!.intrinsicWidth, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
        if (orientationValue == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        orientationValue = orientation
    }

    companion object {

        private val ATTRS = intArrayOf(android.R.attr.listDivider)

        const val HORIZONTAL_LIST = HORIZONTAL

        const val VERTICAL_LIST = VERTICAL
    }
}
