package com.example.customviewsproject.component

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.updateLayoutParams
import com.example.customviewsproject.R
import com.example.customviewsproject.component.data.ProgressData
import com.example.customviewsproject.component.utils.ANIMATION_DURATION

class MarkerViewGroup @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    private val markerView = MarkerView(context).apply {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private val paddingHorizontal by lazy { resources.getDimension(R.dimen.pin_size).toInt() / 2 }

    private var progressData = ProgressData()
    private var percentageInCoordinates = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateMargins()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val markerViewHeight = measureMarkerView(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, markerViewHeight)
    }

    private fun measureMarkerView(widthMeasureSpec: Int, heightMeasureSpec: Int): Int {
        measureChild(markerView, widthMeasureSpec, heightMeasureSpec)
        return markerView.measuredHeight
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val markerWidth = markerView.measuredWidth
        val halfOfMarkerWidth = markerWidth / 2

        val positionX: Int
        val trianglePositionX: Int
        if (percentageInCoordinates > halfOfMarkerWidth) {
            trianglePositionX = halfOfMarkerWidth
            positionX = percentageInCoordinates - halfOfMarkerWidth
        } else {
            trianglePositionX = percentageInCoordinates
            positionX = 0
        }
        markerView.layout(positionX, 0, markerWidth + positionX, markerView.measuredHeight)
        markerView.setCenterX(trianglePositionX)
    }

    private fun updateMargins() {
        updateLayoutParams<MarginLayoutParams> {
            marginStart = paddingHorizontal
            marginEnd = paddingHorizontal
        }
    }

    fun setTitle(progressData: ProgressData) {
        this.progressData = progressData
        removeAllViews()
        addView(markerView)
        startMarkerAnimation(markerView, progressData)
        requestLayout()
    }

    private fun startMarkerAnimation(
        markerView: MarkerView,
        progressData: ProgressData
    ) {
        ValueAnimator.ofFloat(0f, progressData.progress).apply {
            addUpdateListener { animator ->
                val currentStep = animator.animatedValue as Float
                percentageInCoordinates = getCenterX(currentStep)
                val currentValue = (currentStep * 100).toInt() / 100f
                val title = progressData.toValueAndCurrency(currentValue) { it }
                markerView.setTitle(title)
            }
            duration = ANIMATION_DURATION
        }.start()
    }

    private fun getCenterX(progress: Float): Int {
        return (progress * measuredWidth / progressData.max).toInt()
    }

}
