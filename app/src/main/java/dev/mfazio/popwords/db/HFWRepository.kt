package dev.mfazio.popwords.db

class HFWRepository private constructor(private val hfwDao: HFWDao) {

    suspend fun addAttempt(attempt: AttemptEntity) = hfwDao.addAttempt(attempt)

    fun getAttempts() = hfwDao.getAttempts()

    suspend fun clearAttempts() {
        hfwDao.clearAttempts()
    }

    companion object {
        @Volatile
        private var instance: HFWRepository? = null

        fun getInstance(hfwDao: HFWDao) =
            instance ?: synchronized(this) {
                instance ?: HFWRepository(hfwDao).also { instance = it }
            }
    }
}