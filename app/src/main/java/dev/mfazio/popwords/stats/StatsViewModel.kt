package dev.mfazio.popwords.stats

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dev.mfazio.popwords.Attempt
import dev.mfazio.popwords.AttemptType
import dev.mfazio.popwords.db.AttemptEntity
import dev.mfazio.popwords.db.HFWDatabase
import dev.mfazio.popwords.db.HFWRepository

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val attemptEntities: LiveData<List<AttemptEntity>>
    val attempts: LiveData<List<Attempt>>

    val typingSummaryItem = MediatorLiveData<SummaryItem>()
    val voiceSummaryItem = MediatorLiveData<SummaryItem>()

    private val hfwRepository: HFWRepository

    init {
        val dao = HFWDatabase.getInstance(application).hfwDao()
        this.hfwRepository = HFWRepository.getInstance(dao)

        attemptEntities = hfwRepository.getAttempts()

        attempts = Transformations.map(attemptEntities) { entities ->
            entities?.map(AttemptEntity::toAttempt) ?: listOf()
        }

        typingSummaryItem.addSource(attempts) { attempts ->
            Log.wtf("RSI", "Attempts count: ${attempts.count()}")
            if (attempts != null) {
                val typingAttempts = attempts.filter { it.attemptType == AttemptType.Typing }
                typingSummaryItem.value = SummaryItem(
                    SummaryItemType.Typing,
                    typingAttempts.count { it.wasCorrect },
                    typingAttempts.count { !it.wasSkipped },
                    typingAttempts.count { it.wasSkipped }
                )
            }

        }

        voiceSummaryItem.addSource(attempts) { attempts ->
            Log.wtf("VSI", "Attempts: $attempts")
            if (attempts != null) {
                val voiceAttempts = attempts.filter { it.attemptType == AttemptType.Voice }
                voiceSummaryItem.value = SummaryItem(
                    SummaryItemType.Voice,
                    voiceAttempts.count { it.wasCorrect },
                    voiceAttempts.count { !it.wasSkipped },
                    voiceAttempts.count { it.wasSkipped }
                )
            }
        }

    }
}
