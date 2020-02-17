package dev.mfazio.popwords

import android.animation.Animator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath


fun LottieAnimationView.enable() = this.apply {
    addValueCallback(
        KeyPath("**"),
        LottieProperty.COLOR_FILTER,
        {
            null
        }
    )
    playAnimation()
}

fun LottieAnimationView.disable() = this.apply {
    pauseAnimation()
    progress = 0f
    addValueCallback(
        KeyPath("01", "**"),
        LottieProperty.COLOR_FILTER,
        {
            PorterDuffColorFilter(
                Color.GRAY,
                PorterDuff.Mode.SRC_ATOP
            )
        }
    )
}

fun LottieAnimationView.hideOnAnimationComplete(onComplete: () -> Unit) =
    this.addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {}

        override fun onAnimationEnd(p0: Animator?) {
            this@hideOnAnimationComplete.visibility = View.GONE
            onComplete()
        }

        override fun onAnimationCancel(p0: Animator?) {}

        override fun onAnimationStart(p0: Animator?) {}
    })