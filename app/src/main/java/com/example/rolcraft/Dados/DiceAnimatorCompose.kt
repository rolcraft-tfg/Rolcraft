package com.example.rolcraft.Dados

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.abs
import kotlin.random.Random
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.isActive

class DiceAnimatorCompose(private val context: Context) {

    private fun vibrar(ms: Long) {
        val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
        } else vib.vibrate(ms)
    }

    private fun sonido() {
        MediaPlayer.create(context, com.example.rolcraft.R.raw.dice).start()
    }

    suspend fun animateDice(
        dice: DiceType,
        screenWidth: Float,
        screenHeight: Float,
        onUpdateFace: (Int) -> Unit,
        onUpdatePosition: (Offset) -> Unit,
        onUpdateRotation: (Float) -> Unit,
        onFinish: (Int) -> Unit
    ) {
        var pos = Offset(0f, 0f)

        val minSpeed = 4000f
        val maxSpeed = 9000f

        fun randomSpeed() =
            (minSpeed + Random.nextFloat() * (maxSpeed - minSpeed)) *
                    if (Random.nextBoolean()) 1 else -1

        var vel = Offset(
            randomSpeed(),
            randomSpeed()
        )
        sonido()

        var rot = 0f
        val rotationSpeed = 6f

        var friction = 1.0f
        var bounce = -0.94f

        val halfDice = 75f
        val maxX = screenWidth - halfDice
        val maxY = screenHeight - halfDice

        val stopSpeed = 180f

        var bounceCount = 0
        val maxBounces = 20

        while (
            coroutineContext.isActive &&
            (abs(vel.x) > stopSpeed || abs(vel.y) > stopSpeed)
        ) {

            pos += vel * 0.016f
            rot += rotationSpeed

            var collided = false

            if (pos.x > maxX) {
                pos = Offset(maxX, pos.y)
                vel = Offset(vel.x * bounce, vel.y)
                collided = true
            }
            if (pos.x < -maxX) {
                pos = Offset(-maxX, pos.y)
                vel = Offset(vel.x * bounce, vel.y)
                collided = true
            }
            if (pos.y > maxY) {
                pos = Offset(pos.x, maxY)
                vel = Offset(vel.x, vel.y * bounce)
                collided = true
            }
            if (pos.y < -maxY) {
                pos = Offset(pos.x, -maxY)
                vel = Offset(vel.x, vel.y * bounce)
                collided = true
            }

            if (collided) {
                vibrar(10)
                bounceCount++
                bounce *= 0.96f
                friction *= 0.990f
            }

            vel *= friction

            if (bounceCount > maxBounces) break

            val randomFace = (1..dice.faces).random()
            onUpdateFace(dice.images[randomFace - 1])

            onUpdatePosition(pos)
            onUpdateRotation(rot)

            delay(16)
            yield()
        }

        val result = Random.nextInt(1, dice.faces + 1)
        onUpdateFace(dice.images[result - 1])

        vibrar(60)

        onFinish(result)
    }
}
