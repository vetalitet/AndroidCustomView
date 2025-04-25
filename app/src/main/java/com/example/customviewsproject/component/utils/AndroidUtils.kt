package com.example.customviewsproject.component.utils

import android.content.res.Resources
import android.widget.ImageView
import kotlin.math.abs

val Int.px: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
