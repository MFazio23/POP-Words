package dev.mfazio.popwords

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("wordWasSuccessful")
fun bindWordWasSuccessfulTextColor(textView: TextView, wasSuccessful: Boolean?) {
    textView.setTextColor(
        when (wasSuccessful) {
            true -> ContextCompat.getColor(textView.context, R.color.correctWord)
            false -> ContextCompat.getColor(textView.context, R.color.incorrectWord)
            else -> Color.GRAY
        }
    )
}

@BindingAdapter("wordWasSuccessful")
fun bindWordWasSuccessfulImage(imageView: ImageView, wasSuccessful: Boolean) {
    with(imageView) {
        imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                imageView.context,
                if (wasSuccessful) R.color.correctWord else R.color.incorrectWord
            )
        )
        setImageResource(if (wasSuccessful) R.drawable.ic_check_circle_black_24dp else R.drawable.ic_mood_bad_black_24dp)
    }
}

@BindingAdapter("wordWasSkipped")
fun bindWordWasSkippedImage(imageView: ImageView, wasSkipped: Boolean) {
    if (wasSkipped) {
        with(imageView) {
            imageTintList = ColorStateList.valueOf(Color.GRAY)
            setImageResource(R.drawable.ic_skip_next_black_24dp)
            background = null
        }
    }
}