package dev.mfazio.popwords.typing

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import dev.mfazio.popwords.databinding.TypingFragmentBinding
import dev.mfazio.popwords.disable
import dev.mfazio.popwords.enable
import dev.mfazio.popwords.hideOnAnimationComplete
import java.util.Locale


class TypingFragment : Fragment() {

    private val typingViewModel by activityViewModels<TypingViewModel>()
    private lateinit var tts: TextToSpeech
    private lateinit var typingAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = this.context ?: throw Exception("Invalid context.")

        this.typingViewModel.apply {
            this.isSpeaking.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it != true) {
                    typingAnimationView.disable()
                } else {
                    typingAnimationView.enable()
                }
            })
            this.wasCorrect.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    with(successAnimationView) {
                        visibility = View.VISIBLE
                        playAnimation()
                    }
                }
            })
        }

        this.tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.ERROR) {
                Toast.makeText(
                    context,
                    "There was a problem loading the TTS service",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                this.tts.language = Locale.US
            }
        }.apply {
            this.setSpeechRate(0.25f)
            this.setOnUtteranceProgressListener(typingViewModel.utteranceProgressListener)
        }

        val binding = TypingFragmentBinding.inflate(inflater).apply {
            this.viewModel = typingViewModel

            this@TypingFragment.typingAnimationView = this.typingAnimationView.apply {
                this.disable()
                this.setOnClickListener {
                    if (viewModel?.isSpeaking?.value != true) {
                        this.enable()
                        viewModel?.currentWord?.value?.let { currentWord ->
                            tts.speak(
                                currentWord,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                "typing"
                            )
                        }
                    }
                }
            } ?: throw Exception("Sound animation view not found")

            this@TypingFragment.successAnimationView = successAnimationView.apply {
                this.hideOnAnimationComplete() {
                    typingViewModel.getNewWord()
                }
            }

            typingSubmitText.setOnClickListener {
                viewModel?.submitEntry()
                hideKeyboard()
            }

            typingEditText.setOnKeyListener { _, keyCode, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    viewModel?.submitEntry()
                    true
                } else {
                    false
                }
            }

            lifecycleOwner = this@TypingFragment
        }

        return binding.root
    }

    override fun onPause() {
        if (::tts.isInitialized) {
            this.tts.stop()
            this.tts.shutdown()
        }
        super.onPause()
    }

    private fun hideKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let { inputManager ->
            activity?.currentFocus?.let { view ->
                inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}
