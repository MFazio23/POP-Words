package dev.mfazio.popwords.voice

import android.app.Application
import android.speech.RecognitionListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.mfazio.popwords.WordRecognitionListener
import dev.mfazio.popwords.db.AttemptEntity
import dev.mfazio.popwords.AttemptType
import dev.mfazio.popwords.db.HFWDatabase
import dev.mfazio.popwords.db.HFWRepository
import dev.mfazio.popwords.getRandomWord
import kotlinx.coroutines.launch

class VoiceViewModel(application: Application) : AndroidViewModel(application) {
    var currentWord: MutableLiveData<String> = MutableLiveData(getRandomWord())
    var errorText: MutableLiveData<String> = MutableLiveData()
    var wasCorrect: MutableLiveData<Boolean?> = MutableLiveData(null)
    val isListening: MutableLiveData<Boolean> = MutableLiveData(false)
    val recognitionListener: RecognitionListener = WordRecognitionListener(this)

    private val hfwRepository: HFWRepository

    init {
        val dao = HFWDatabase.getInstance(application).hfwDao()
        this.hfwRepository = HFWRepository.getInstance(dao)
    }
    fun handleResult(result: List<String>?) {
        this.isListening.value = false
        result?.let { resultWords ->

            val guessWasCorrect = resultWords.contains(currentWord.value)
            wasCorrect.value = guessWasCorrect

            viewModelScope.launch {
                hfwRepository.addAttempt(
                    AttemptEntity(
                        sightWord = currentWord.value ?: "Unknown",
                        answer = resultWords.joinToString(", "),
                        wasCorrect = guessWasCorrect,
                        attemptType = AttemptType.Voice
                    )
                )
            }
        } ?: handleError("I'm sorry, I didn't understand what you said.  Please try again.")
    }

    fun skipWord() {
        viewModelScope.launch {
            hfwRepository.addAttempt(
                AttemptEntity(
                    sightWord = currentWord.value ?: "Unknown",
                    wasSkipped = true,
                    attemptType = AttemptType.Voice
                )
            )
        }

        this.getNewWord()
    }

    fun getNewWord() {
        currentWord.value = getRandomWord()
        wasCorrect.value = null
    }

    fun handleError(errorText: String) {
        this.isListening.value = false
        this.errorText.value = errorText
    }
}
