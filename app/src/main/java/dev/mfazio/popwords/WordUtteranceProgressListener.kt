package dev.mfazio.popwords

import android.speech.tts.UtteranceProgressListener
import dev.mfazio.popwords.typing.TypingViewModel

class WordUtteranceProgressListener(private val typingViewModel: TypingViewModel) :
    UtteranceProgressListener() {
    override fun onDone(p0: String?) {
        typingViewModel.speakingStatusChange(false)
    }

    override fun onError(p0: String?) {
        typingViewModel.speakingStatusChange(false)
    }

    override fun onStart(p0: String?) {
        typingViewModel.speakingStatusChange(true)
    }
}