package com.example.anuar_countdowntimerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var remainingTime = 60
    private var timerActive = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable
    private lateinit var countdownInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countdownInput = findViewById(R.id.countdown_input)
        val timerText: TextView = findViewById(R.id.countdown_timer_text)
        val circleContainer: View = findViewById(R.id.circle_container)
        val startStopButton: Button = findViewById(R.id.start_stop_button)
        val resetButton: Button = findViewById(R.id.reset_button)

        timerRunnable = object : Runnable {
            override fun run() {
                if (remainingTime > 0) {
                    remainingTime--
                    timerText.text = "$remainingTime sec"
                    handler.postDelayed(this, 1000)
                } else {
                    timerText.text = "Time's up!"
                    stopTimer()
                    startStopButton.visibility = Button.GONE
                }
            }
        }

        startStopButton.setOnClickListener {
            val inputTime = countdownInput.text.toString()

            if (!timerActive) {
                if (remainingTime == 60 && inputTime.isNotEmpty()) {
                    try {
                        remainingTime = inputTime.toInt()
                        countdownInput.visibility = EditText.GONE
                        circleContainer.visibility = View.VISIBLE
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this, "Invalid time entered", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                timerActive = true
                startStopButton.text = "Stop"
                handler.post(timerRunnable)
            } else {
                stopTimer()
                startStopButton.text = "Start"
            }
        }

        resetButton.setOnClickListener {
            stopTimer()
            resetTimer(timerText, startStopButton, circleContainer)
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        timerActive = false
    }

    private fun resetTimer(timerText: TextView, startStopButton: Button, circleContainer: View) {
        remainingTime = 60
        timerActive = false
        startStopButton.text = "Start"
        countdownInput.visibility = EditText.VISIBLE
        circleContainer.visibility = View.GONE
        startStopButton.visibility = Button.VISIBLE
    }
}

