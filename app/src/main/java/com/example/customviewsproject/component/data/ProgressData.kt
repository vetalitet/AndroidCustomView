package com.example.customviewsproject.component.data

data class ProgressData(
    val targetItems: List<Int> = emptyList(),
    val currency: String = "zł",
    val progress: Float = 0f
) {

    val max: Int = targetItems.maxOrNull() ?: 0

    fun getProgressInPercents(currentProgress: Float): Float {
        val percent: Float = currentProgress * 100 / max
        return percent / 100f
    }

    fun toValueAndCurrency(): String {
        return "$progress $currency"
    }

    fun <T : Number, R : Number> toValueAndCurrency(
        value: T,
        valueConverter: (T) -> R
    ): String {
        return "${valueConverter(value)} $currency"
    }

}
