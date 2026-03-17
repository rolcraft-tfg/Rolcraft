package com.example.rolcraft

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.pruebafisicasdados.DiceLibrary
import com.example.rolcraft.dados.DiceAnimator
import com.example.rolcraft.dados.DiceType

class MainActivity : ComponentActivity() {

    private lateinit var overlay: FrameLayout
    private lateinit var animator: DiceAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        overlay = findViewById(R.id.diceOverlay)
        animator = DiceAnimator(this)

        findViewById<Button>(R.id.btnD4).setOnClickListener { lanzar(DiceLibrary.D4) }
        findViewById<Button>(R.id.btnD6).setOnClickListener { lanzar(DiceLibrary.D6) }
        findViewById<Button>(R.id.btnD8).setOnClickListener { lanzar(DiceLibrary.D8) }
        findViewById<Button>(R.id.btnD10).setOnClickListener { lanzar(DiceLibrary.D10) }
        findViewById<Button>(R.id.btnD12).setOnClickListener { lanzar(DiceLibrary.D12) }
        findViewById<Button>(R.id.btnD20).setOnClickListener { lanzar(DiceLibrary.D20) }
    }

    private fun lanzar(dice: DiceType) {
        val image = ImageView(this)
        image.layoutParams = FrameLayout.LayoutParams(150, 150)
        image.setImageResource(dice.images.random())

        overlay.addView(image)

        overlay.post {
            image.x = (0..(overlay.width - 150)).random().toFloat()
            image.y = (0..(overlay.height - 150)).random().toFloat()

            val result = (1..dice.faces).random()
            animator.animateDice(image, dice, result)
        }
    }
}
