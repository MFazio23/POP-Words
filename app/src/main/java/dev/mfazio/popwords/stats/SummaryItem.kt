package dev.mfazio.popwords.stats

import java.math.BigDecimal
import java.math.RoundingMode

data class SummaryItem(
    val type: SummaryItemType,
    val correct: Int,
    val total: Int,
    val skips: Int
) {
    val typeText = "${this.type}:"
    val percentage =
        if(total != 0) {
            BigDecimal(correct / total.toDouble() * 100).setScale(2, RoundingMode.HALF_EVEN)
                .toDouble()
        } else {
            0.0
        }
    val percentageText = "($percentage%)"
    val ratio = "$correct/$total"
    val skipsText = "Skipped $skips times."
}

enum class SummaryItemType {
    Typing,
    Voice
}