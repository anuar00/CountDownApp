package com.example.anuar_countdowntimerapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
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
        val startStopButton: Button = findViewById(R.id.start_stop_button)
        val resetButton: Button = findViewById(R.id.reset_button)

        timerRunnable = object : Runnable {
            override fun run() {
                if (remainingTime > 0) {
                    remainingTime--
                    timerText.text = "$remainingTime seconds remaining"
                    handler.postDelayed(this, 1000)
                } else {
                    timerText.text = "Time's up!"
                    startStopButton.visibility = Button.GONE

                    stopTimer()
                }
            }
        }

        startStopButton.setOnClickListener {
            val inputTime = countdownInput.text.toString()

            if (!TextUtils.isEmpty(inputTime)) {
                try {
                    remainingTime = inputTime.toInt()

                    if (!timerActive) {
                        timerActive = true
                        startStopButton.text = "Stop"
                        countdownInput.visibility = EditText.GONE
                        timerText.visibility = TextView.VISIBLE
                        handler.post(timerRunnable)
                    } else {
                        stopTimer()
                        startStopButton.text = "Start"
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Invalid time entered", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a time", Toast.LENGTH_SHORT).show()
            }
        }

        resetButton.setOnClickListener {
            stopTimer()
            resetTimer(timerText, startStopButton)
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        timerActive = false
    }

    private fun resetTimer(timerText: TextView, startStopButton: Button) {
        remainingTime = 60
        timerActive = false
        startStopButton.text = "Start"
        countdownInput.visibility = EditText.VISIBLE
        timerText.visibility = TextView.GONE
        startStopButton.visibility = Button.VISIBLE
    }
}