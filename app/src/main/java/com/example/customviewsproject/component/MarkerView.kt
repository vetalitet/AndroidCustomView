package com.example.customviewsproject.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.example.customviewsproject.R
import com.example.customviewsproject.component.utils.dp

class MarkerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val titleTextSize by lazy { resources.getDimension(R.dimen.marker_title_text_size) }
    private val triangleHeight by lazy {
        val height = resources.getDimension(R.dimen.marker_triangle_height).toInt()
        height + (height % 2)
    }
    private var bodyHeight = 0f
    private var totalHeight = 0
    private var totalWidth = 0
    private val titleMarginHorizontal = 4.dp

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.marker_color)
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = titleTextSize
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private var title: String = ""
    private var triangleCenterX = 0

    private val textBounds = Rect()

    private val p1 = PointF()
    private val p2 = PointF()
    private val p3 = PointF()
    private val path = Path()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        textPaint.getTextBounds(title, 0, title.length, textBounds)

        val fontMetrics = textPaint.fontMetrics
        bodyHeight = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading
        totalHeight = triangleHeight + bodyHeight.toInt()
        totalWidth = textBounds.width() + titleMarginHorizontal * 2

        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), bodyHeight, bgPaint)
        canvas.drawText(title, titleMarginHorizontal.toFloat(), 40f, textPaint)
        drawTriangle(bodyHeight, triangleCenterX, triangleHeight, bgPaint, canvas)
    }

    private fun drawTriangle(startHeight: Float, x: Int, size: Int, p: Paint, canvas: Canvas) {
        p1.set(x - size.toFloat(), startHeight)
        p2.set(x + size.toFloat(), startHeight)
        p3.set(x.toFloat(), startHeight + size.toFloat())

        path.reset()
        path.moveTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
        path.close()

        canvas.drawPath(path, p)
    }

    fun setTitle(title: String) {
        this.title = title
        requestLayout()
    }

    fun setCenterX(triangleCenterX: Int) {
        this.triangleCenterX = triangleCenterX
        invalidate()
    }

}
