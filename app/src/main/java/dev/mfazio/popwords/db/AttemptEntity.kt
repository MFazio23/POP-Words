package dev.mfazio.popwords.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mfazio.popwords.Attempt
import dev.mfazio.popwords.AttemptType
import java.util.*

@Entity(tableName = "attempts")
class AttemptEntity(
    @PrimaryKey(autoGenerate = true) var attemptId: Long = 0,
    var sightWord: String,
    var answer: String = "",
    var wasCorrect: Boolean = false,
    var wasSkipped: Boolean = false,
    var timestamp: Date? = Date(),
    var attemptType: AttemptType = AttemptType.Unknown
) {
    fun toAttempt(): Attempt =
        Attempt(
            attemptId,
            sightWord,
            answer,
            wasCorrect,
            wasSkipped,
            timestamp ?: Date(),
            attemptType
        )
}