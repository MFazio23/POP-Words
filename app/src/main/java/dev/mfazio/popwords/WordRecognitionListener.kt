package dev.mfazio.popwords

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import dev.mfazio.popwords.voice.VoiceViewModel

class WordRecognitionListener(private val viewModel: VoiceViewModel) : RecognitionListener {

    companion object {
        val TAG = WordRecognitionListener::class.java.name
    }

    override fun onReadyForSpeech(p0: Bundle?) {}

    override fun onRmsChanged(p0: Float) {}

    override fun onBufferReceived(p0: ByteArray?) {}

    override fun onPartialResults(p0: Bundle?) {}

    override fun onEvent(p0: Int, p1: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onEndOfSpeech() {}

    override fun onError(errorCode: Int) {
        val errorText = when(errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio error"
            SpeechRecognizer.ERROR_CLIENT -> "Client error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Audio permission has not been granted.  Please reinstall the app."
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "There was a network issue.  Make sure you're online"
            SpeechRecognizer.ERROR_NO_MATCH -> "I'm sorry, I didn't catch that.  Please try again!"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "I'm sorry, I didn't catch that.  Please try again!"
            else -> "An error has occurred."
        }

        viewModel.handleError(errorText)
    }

    override fun onResults(p0: Bundle?) {
        viewModel.handleResult(p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.filterNotNull())
    }
}