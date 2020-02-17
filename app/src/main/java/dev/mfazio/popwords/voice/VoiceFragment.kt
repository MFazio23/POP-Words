package dev.mfazio.popwords.voice

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import dev.mfazio.popwords.databinding.VoiceFragmentBinding
import dev.mfazio.popwords.disable
import dev.mfazio.popwords.enable
import dev.mfazio.popwords.hideOnAnimationComplete

class VoiceFragment : Fragment() {

    companion object {
        const val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 7641
    }

    //by viewModels() is scoped to the fragment, by activityViewModels is scoped to the activity
    //private val voiceViewModel by viewModels<VoiceViewModel>()
    private val voiceViewModel by activityViewModels<VoiceViewModel>()
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var listenAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = this.context ?: throw Exception("Invalid context.")
        val activity = this.activity ?: throw Exception("Invalid activity.")

        this.voiceViewModel.apply {
            this.isListening.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it != true) {
                    listenAnimationView.disable()
                } else {
                    listenAnimationView.enable()
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

            this.errorText.observe(viewLifecycleOwner, Observer<String> {
                if (it != null) {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            })
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(voiceViewModel.recognitionListener)

        val binding = VoiceFragmentBinding.inflate(layoutInflater).apply {
            this.viewModel = voiceViewModel

            this@VoiceFragment.listenAnimationView = listenAnimationView.apply {
                this.disable()
                this.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        toggleListening()
                    } else {
                        activity.requestPermissions(
                            arrayOf(android.Manifest.permission.RECORD_AUDIO),
                            MY_PERMISSIONS_REQUEST_RECORD_AUDIO
                        )
                    }

                }
            } ?: throw Exception("Loading view not found.")

            this@VoiceFragment.successAnimationView = successAnimationView.apply {
                this.hideOnAnimationComplete {
                    voiceViewModel.getNewWord()
                }
            }

            lifecycleOwner = this@VoiceFragment
        }

        return binding.main
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.any() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                toggleListening()
            }
        }
    }

    private fun toggleListening() {
        if (voiceViewModel.isListening.value == true) {
            stopListening()
            this.listenAnimationView.disable()
        } else {
            startListening()
            this.listenAnimationView.enable()
        }
    }

    private fun startListening() {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

        speechRecognizer.startListening(recognizerIntent)
        voiceViewModel.isListening.value = true
    }

    private fun stopListening() {
        speechRecognizer.stopListening()
    }

    override fun onDestroy() {
        speechRecognizer.destroy()
        super.onDestroy()
    }
}
