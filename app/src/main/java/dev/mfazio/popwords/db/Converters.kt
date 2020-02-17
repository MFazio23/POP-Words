package dev.mfazio.popwords.db

import androidx.room.TypeConverter
import dev.mfazio.popwords.AttemptType
import java.util.*

class Converters {

    @TypeConverter
    fun dateFromTimestamp(timestamp: Long?): Date? = timestamp?.let { Date(it) }

    @TypeConverter
    fun timestampFromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun stringToAttemptType(typeString: String) = AttemptType.valueOf(typeString)

    @TypeConverter
    fun attemptTypeToString(attemptType: AttemptType) = attemptType.name
}