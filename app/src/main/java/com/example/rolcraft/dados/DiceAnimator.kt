package com.example.rolcraft.dados

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.example.rolcraft.R

class DiceAnimator(private val context: Context) {

    private fun vibrar(ms: Long) {
        val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
        } else vib.vibrate(ms)
    }

    private fun sonido() {
        MediaPlayer.create(context, R.raw.dice).start()
    }

    private fun bounce(imageView: ImageView) {
        val anim = ScaleAnimation(
            1f, 0.9f, 1f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.duration = 120
        anim.repeatCount = 1
        anim.repeatMode = Animation.REVERSE
        imageView.startAnimation(anim)
    }

    fun animateDice(
        imageView: ImageView,
        dice: DiceType,
        result: Int,
        onFinish: () -> Unit
    ) {
        val parent = imageView.parent as View
        val maxX = parent.width - imageView.width
        val maxY = parent.height - imageView.height

        imageView.alpha = 0.6f

        fun mover(rebotes: Int) {
            if (rebotes == 0) {

                imageView.setImageResource(dice.images[result - 1])
                imageView.alpha = 1f
                bounce(imageView)
                vibrar(60)

                imageView.post { onFinish() }
                return
            }

            val randomX = (0..maxX).random().toFloat()
            val randomY = (0..maxY).random().toFloat()

            val anim = TranslateAnimation(
                0f, randomX - imageView.x,
                0f, randomY - imageView.y
            )

            anim.duration = 150
            anim.fillAfter = true

            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    imageView.rotation += 90f
                    val randomFace = (1..dice.faces).random()
                    imageView.setImageResource(dice.images[randomFace - 1])
                    vibrar(10)
                    sonido()
                }

                override fun onAnimationEnd(animation: Animation?) {
                    imageView.x = randomX
                    imageView.y = randomY
                    mover(rebotes - 1)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })

            imageView.startAnimation(anim)
        }

        mover(8)
    }
}
