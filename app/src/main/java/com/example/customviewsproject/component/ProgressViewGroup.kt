package com.example.customviewsproject.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.example.customviewsproject.R
import com.example.customviewsproject.component.PinDrawable.DONE_PIN
import com.example.customviewsproject.component.PinDrawable.TODO_PIN
import com.example.customviewsproject.component.data.ProgressData
import com.example.customviewsproject.component.utils.ANIMATION_DURATION

class ProgressViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    private var progressData = ProgressData()

    private val pinSize by lazy { resources.getDimension(R.dimen.pin_size).toInt() }
    private val pinPadding by lazy { resources.getDimension(R.dimen.pin_padding).toInt() }
    private val pinMargin by lazy { resources.getDimension(R.dimen.pin_margin).toInt() }
    private val progressLineHeight by lazy {
        resources.getDimension(R.dimen.progress_line_height).toInt()
    }
    private var halfPinSize: Int = pinSize / 2
    private var progressWidth: Int = 0

    private val progressView = ProgressView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }
    private val pinViews = mutableListOf<ImageView>()

    private val todoPinDrawable by lazy { createGradientDrawable(TODO_PIN) }
    private val donePinDrawable by lazy { createGradientDrawable(DONE_PIN) }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        layoutProgressView()
        layoutPinViews()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureProgressView(widthMeasureSpec, heightMeasureSpec)
        val maxHeight = measurePins(widthMeasureSpec, heightMeasureSpec) + pinMargin * 2
        setMeasuredDimension(widthMeasureSpec, maxHeight)
    }

    private fun layoutProgressView() {
        val top = (pinSize + pinMargin * 2 - progressView.measuredHeight) / 2
        progressView.layout(
            halfPinSize,
            top,
            progressView.measuredWidth + halfPinSize,
            top + progressView.measuredHeight
        )
    }

    private fun layoutPinViews() {
        pinViews.forEachIndexed { index, view ->
            val targetProgress = progressData.targetItems[index]
            val progressInPercents = progressData.getProgressInPercents(targetProgress.toFloat())
            val centerPin = progressWidth * progressInPercents + halfPinSize
            view.layout(
                (centerPin - view.measuredWidth / 2).toInt(),
                pinMargin,
                (centerPin + view.measuredWidth / 2).toInt(),
                view.measuredHeight + pinMargin
            )
        }
        if (childCount > 0) {
            startProgressAnimation()
        }
    }

    private fun startProgressAnimation() {
        ValueAnimator.ofFloat(0f, progressData.progress).apply {
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                val progress = animatedValue * 100 / progressData.targetItems.max()
                val progressInCoordinates = progress * measuredWidth / 100
                progressView.setProgress(progress)

                pinViews.filter { it.x < progressInCoordinates }.forEach {
                    it.background = donePinDrawable
                    it.setImageDrawable(getDrawable(resources, DONE_PIN.icon, context.theme))
                }
            }
            duration = ANIMATION_DURATION
        }.start()
    }

    private fun measureProgressView(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(progressView, widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY) - pinSize
        val height = MeasureSpec.makeMeasureSpec(progressLineHeight, MeasureSpec.EXACTLY)
        progressView.measure(width, height)
        progressWidth = progressView.measuredWidth
    }

    private fun measurePins(widthMeasureSpec: Int, heightMeasureSpec: Int): Int {
        /*var maxHeight = 0
        pinViews.forEachIndexed { index, child ->
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            maxHeight = child.measuredHeight
        }
        return maxHeight*/
        return pinViews.maxOfOrNull { child ->
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            child.measuredHeight
        } ?: 0
    }

    fun setProgressData(progressData: ProgressData) {
        this.progressData = progressData
        removeAllViews()

        addProgressView(progressData)
        addPins(progressData)
    }

    private fun addProgressView(progressData: ProgressData) {
        val inactiveColor = getColor(resources, R.color.inactive_progress_line_bg, context.theme)
        val activeColor = getColor(resources, R.color.active_progress_line_bg, context.theme)
        progressView.setBackgroundLineColor(inactiveColor)
        progressView.setProgressLineColor(activeColor)
        progressView.setCornerRadius(12f)
        addView(progressView)
    }

    private fun addPins(progressData: ProgressData) {
        pinViews.clear()
        repeat(progressData.targetItems.size) {
            val pin = createPin()
            pinViews.add(pin)
            addView(pin)
        }
    }

    private fun createPin(): ImageView {
        return ImageView(context).apply {
            layoutParams = LayoutParams(pinSize, pinSize)
            setPadding(pinPadding, pinPadding, pinPadding, pinPadding)
            background = todoPinDrawable
            setImageDrawable(getDrawable(resources, TODO_PIN.icon, context.theme))
        }
    }

    private fun createGradientDrawable(pinDrawable: PinDrawable): GradientDrawable {
        val drawablePin = R.drawable.drawable_pin
        val drawable = getDrawable(resources, drawablePin, context.theme) as GradientDrawable
        drawable.setColor(resources.getColor(pinDrawable.backgroundColor, context.theme))
        return drawable
    }

}

enum class PinDrawable(
    @ColorRes val backgroundColor: Int,
    @DrawableRes val icon: Int
) {
    DONE_PIN(
        R.color.done_pin_background,
        R.drawable.ic_check
    ),
    TODO_PIN(
        R.color.todo_pin_background,
        R.drawable.ic_giftcard
    )
}
