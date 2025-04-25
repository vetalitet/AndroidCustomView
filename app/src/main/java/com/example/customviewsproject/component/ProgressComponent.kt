package com.example.customviewsproject.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.customviewsproject.component.data.ProgressData
import com.example.customviewsproject.databinding.LayoutProgressComponentBinding

class ProgressComponent @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var binding: LayoutProgressComponentBinding =
        LayoutProgressComponentBinding.inflate(LayoutInflater.from(context), this)
    private var progressData = ProgressData()

    init {
        orientation = VERTICAL
    }

    fun setProgressData(progressData: ProgressData) {
        this.progressData = progressData
        binding.progressViewGroup.setProgressData(progressData)
        binding.textsViewGroup.setProgressData(progressData)
        binding.markerViewGroup.setTitle(progressData)
    }

}
