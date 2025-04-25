package com.example.customviewsproject.component

import android.content.Context
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.children
import com.example.customviewsproject.R
import com.example.customviewsproject.component.data.ProgressData

class TextsViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    private val pinTextSize by lazy { resources.getDimension(R.dimen.pin_text_size) }
    private val pinSize by lazy { resources.getDimension(R.dimen.pin_size).toInt() }
    private val halfPinSize by lazy { pinSize / 2 }
    private var progressData = ProgressData()
    private val textViewLocations = mutableListOf<RectF>()
    private var totalWidth: Int = 0

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        textViewLocations.clear()
        totalWidth = width - pinSize
        children.forEachIndexed { index, child ->
            val targetQuantity = progressData.targetItems[index]
            val progressInPercents = progressData.getProgressInPercents(targetQuantity.toFloat())
            val targetItemPositionIn = totalWidth * progressInPercents
            val halfChildWidth = child.measuredWidth / 2f

            val fromX = targetItemPositionIn + halfPinSize - halfChildWidth
            val toX = fromX + child.measuredWidth

            val fromXOrZero = when {
                fromX < 0 -> 0
                toX > width -> width - child.measuredWidth
                else -> fromX.toInt()
            }
            val toXOrMax = fromXOrZero + child.measuredWidth

            val boundedRect = toRectF(fromXOrZero, 0, toXOrMax, child.measuredHeight)
            if (textViewLocations.intersectsWith(boundedRect)) {
                (child as? TextView)?.text = ""
            } else {
                textViewLocations.add(boundedRect)
            }

            child.layout(fromXOrZero, 0, toXOrMax, child.measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var maxHeight = 0
        children.forEach { child ->
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            maxHeight = child.measuredHeight
        }
        setMeasuredDimension(widthMeasureSpec, maxHeight)
    }

    fun setProgressData(progressData: ProgressData) {
        this.progressData = progressData
        removeAllViews()
        addTexts(progressData)
        requestLayout()
    }

    private fun addTexts(progressData: ProgressData) {
        progressData.targetItems.forEach { value ->
            val textsView = TextView(context)
            textsView.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            textsView.text = progressData.toValueAndCurrency(value) { value }
            textsView.textSize = pinTextSize
            textsView.setTextColor(context.getColor(R.color.pin_text_color))
            textsView.typeface = Typeface.DEFAULT_BOLD
            addView(textsView)
        }
    }

    private fun toRectF(left: Int, top: Int, right: Int, bottom: Int): RectF {
        return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    private fun MutableList<RectF>.intersectsWith(rect: RectF): Boolean {
        return any { it.intersect(rect) }
    }

}
