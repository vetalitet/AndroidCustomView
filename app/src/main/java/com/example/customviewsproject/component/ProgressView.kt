package com.example.customviewsproject.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.customviewsproject.R
import com.example.customviewsproject.component.utils.ANIMATION_DURATION

class ProgressView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val progressLineHeight by lazy {
        resources.getDimension(R.dimen.progress_line_height).toInt()
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val backgroundRect = RectF()
    private val progressRect = RectF()

    private var backgroundColor: Int = 0
    private var progressColor: Int = 0
    private var cornerRadius: Float = 0f
    private var progress: Float = 0f

    init {
        initAttributes(context, attributeSet)
        backgroundPaint.color = backgroundColor
        progressPaint.color = progressColor
    }

    private fun initAttributes(context: Context, attributeSet: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.ProgressView, 0, 0)
        backgroundColor = attributes.getColor(
            R.styleable.ProgressView_inactive_progress_bg,
            resources.getColor(R.color.default_inactive_progress_line_bg, context.theme)
        )
        progressColor = attributes.getColor(
            R.styleable.ProgressView_active_progress_bg,
            resources.getColor(R.color.default_active_progress_line_bg, context.theme)
        )
        cornerRadius = attributes.getInteger(R.styleable.ProgressView_corner_radius, 0).toFloat()
        progress = attributes.getFloat(R.styleable.ProgressView_progress, 0f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(0, widthMeasureSpec)
        val height = resolveSize(progressLineHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        backgroundRect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val top = width / 100f * progress
        progressRect.set(0f, 0f, top, height.toFloat())

        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint)
        canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, progressPaint)
    }

    fun setBackgroundLineColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    fun setProgressLineColor(color: Int) {
        progressPaint.color = color
        invalidate()
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }

    fun setProgress(progress: Float) {
        /*ValueAnimator.ofFloat(0f, progress).apply {
            addUpdateListener { animator ->
                this@ProgressView.progress = animator.animatedValue as Float
                invalidate()
            }
            duration = ANIMATION_DURATION
        }.start()*/
        this.progress = progress
        invalidate()
    }

}
