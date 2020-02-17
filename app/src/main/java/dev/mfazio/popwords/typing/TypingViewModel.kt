package dev.mfazio.popwords.typing

import android.app.Application
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.mfazio.popwords.WordUtteranceProgressListener
import dev.mfazio.popwords.db.AttemptEntity
import dev.mfazio.popwords.AttemptType
import dev.mfazio.popwords.db.HFWDatabase
import dev.mfazio.popwords.db.HFWRepository
import dev.mfazio.popwords.getRandomWord
import kotlinx.coroutines.launch

class TypingViewModel(application: Application) : AndroidViewModel(application) {
    val utteranceProgressListener: UtteranceProgressListener = WordUtteranceProgressListener(this)
    var currentWord = MutableLiveData(getRandomWord())
    var currentGuess: MutableLiveData<String?> = MutableLiveData(null)
    var guessedWord = MutableLiveData("???")
    val isSpeaking: MutableLiveData<Boolean> = MutableLiveData(false)
    var wasCorrect: MutableLiveData<Boolean?> = MutableLiveData(null)

    private val hfwRepository: HFWRepository

    init {
        val dao = HFWDatabase.getInstance(application).hfwDao()
        this.hfwRepository = HFWRepository.getInstance(dao)
    }

    fun getNewWord() {
        this.currentWord.value = getRandomWord()
        this.wasCorrect.value = null
        this.isSpeaking.value = false
        this.currentGuess.value = null
        this.guessedWord.value = "???"
    }

    fun submitEntry() {
        val word = currentGuess.value?.trim()
        val guessWasCorrect = word.equals(currentWord.value, true)

        wasCorrect.value = guessWasCorrect
        guessedWord.value = word

        viewModelScope.launch {
            hfwRepository.addAttempt(
                AttemptEntity(
                    sightWord = currentWord.value ?: "Unknown",
                    answer = word ?: "Unknown",
                    wasCorrect = guessWasCorrect,
                    attemptType = AttemptType.Typing
                )
            )
        }

        /*if (guessWasCorrect) {
            *//*Handler().postDelayed(
                {
                    getNewWord()
                    wasCorrect.value = null
                    guessedWord.value = "???"
                    currentGuess.value = null
                }, 2000
            )*//*
        }*/
    }

    fun skipWord() {
        viewModelScope.launch {
            hfwRepository.addAttempt(
                AttemptEntity(
                    sightWord = currentWord.value ?: "Unknown",
                    wasSkipped = true,
                    attemptType = AttemptType.Typing
                )
            )
        }

        this.getNewWord()
    }

    fun speakingStatusChange(status: Boolean) {
        this.isSpeaking.postValue(status)
    }
}
