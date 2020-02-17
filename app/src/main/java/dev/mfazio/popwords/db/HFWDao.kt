package dev.mfazio.popwords.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HFWDao {

    @Query("SELECT * FROM attempts ORDER BY timestamp DESC")
    fun getAttempts(): LiveData<List<AttemptEntity>>

    @Insert
    suspend fun addAttempt(attempt: AttemptEntity)

    @Query("DELETE FROM attempts")
    suspend fun clearAttempts()
}