package dev.mfazio.popwords

import java.text.SimpleDateFormat
import java.util.*

data class Attempt(
    val attemptId: Long,
    var sightWord: String,
    var answer: String,
    var wasCorrect: Boolean,
    var wasSkipped: Boolean = false,
    var timestamp: Date = Date(),
    var attemptType: AttemptType = AttemptType.Unknown
) {

    val timestampString = dateFormat.format(timestamp)
    val attemptTypeLetter =
        when (attemptType) {
            AttemptType.Typing -> "T"
            AttemptType.Voice -> "V"
            else -> "?"
        }

    val answerText = if(wasSkipped) "<skipped>" else answer

    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aaa", Locale.US)
    }
}