package com.example.rolcraft

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import com.example.rolcraft.dados.DiceAnimator
import com.example.rolcraft.dados.DiceLibrary
import com.example.rolcraft.dados.DiceType

class MainActivity : ComponentActivity() {

    private lateinit var overlay: FrameLayout
    private lateinit var animator: DiceAnimator
    private lateinit var resultPanel: LinearLayout
    private lateinit var resultText: TextView
    private lateinit var btnAceptar: Button

    private var currentDiceView: ImageView? = null   // ← dado actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        overlay = findViewById(R.id.diceOverlay)
        animator = DiceAnimator(this)

        resultPanel = findViewById(R.id.resultPanel)
        resultText = findViewById(R.id.resultText)
        btnAceptar = findViewById(R.id.btnAceptar)

        btnAceptar.setOnClickListener {
            resultPanel.visibility = View.GONE
            overlay.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnD4).setOnClickListener { lanzar(DiceLibrary.D4) }
        findViewById<Button>(R.id.btnD6).setOnClickListener { lanzar(DiceLibrary.D6) }
        findViewById<Button>(R.id.btnD8).setOnClickListener { lanzar(DiceLibrary.D8) }
        findViewById<Button>(R.id.btnD10).setOnClickListener { lanzar(DiceLibrary.D10) }
        findViewById<Button>(R.id.btnD12).setOnClickListener { lanzar(DiceLibrary.D12) }
        findViewById<Button>(R.id.btnD20).setOnClickListener { lanzar(DiceLibrary.D20) }
    }

    private fun lanzar(dice: DiceType) {

        overlay.visibility = View.VISIBLE
        resultPanel.visibility = View.GONE

        // BORRAR SOLO EL DADO ANTERIOR
        currentDiceView?.let { overlay.removeView(it) }

        // CREAR NUEVO DADO
        val image = ImageView(this)
        image.layoutParams = FrameLayout.LayoutParams(150, 150)
        image.setImageResource(dice.images.random())
        overlay.addView(image)
        currentDiceView = image

        overlay.post {
            image.x = (0..(overlay.width - 150)).random().toFloat()
            image.y = (0..(overlay.height - 150)).random().toFloat()

            val result = (1..dice.faces).random()

            animator.animateDice(image, dice, result) {

                moverAlCentro(image) {

                    // COLOCAR PANEL DEBAJO DEL DADO
                    resultPanel.visibility = View.VISIBLE
                    resultPanel.bringToFront()

                    resultText.text = "El resultado es: $result!"

                    resultPanel.post {
                        resultPanel.x = (overlay.width - resultPanel.width) / 2f
                        resultPanel.y = image.y + image.height + 20f
                    }
                }
            }
        }
    }

    private fun moverAlCentro(image: ImageView, onFinish: () -> Unit) {

        overlay.post {

            val centerX = (overlay.width - image.width) / 2f
            val centerY = (overlay.height - image.height) / 2f - 80f

            val animX = ObjectAnimator.ofFloat(image, View.X, image.x, centerX)
            val animY = ObjectAnimator.ofFloat(image, View.Y, image.y, centerY)

            animX.duration = 700
            animY.duration = 700

            animY.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    image.x = centerX
                    image.y = centerY
                    onFinish()
                }
            })

            animX.start()
            animY.start()
        }
    }
}
