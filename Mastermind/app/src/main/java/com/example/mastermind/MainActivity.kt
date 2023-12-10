package com.example.mastermind

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

import android.util.Log
import android.util.Log.ASSERT
private const val TAG = "Mastermind"

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val switchButton = findViewById<Button>(R.id.switch_play) as Button

        switchButton.setOnClickListener {
            switchActivities()
        }

        val exit = findViewById<Button>(R.id.exit)
        exit.setOnClickListener {
            BackgroundMusicService.mediaPlayer.stop()
            finishAffinity()
            System.exit(0)
        }

        val optionsBtn = findViewById<Button>(R.id.options_btn)
        optionsBtn.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val intent = Intent(this, BackgroundMusicService::class.java)
        startService(intent)
    }

    private fun switchActivities() {
        val intent = Intent(this, PlayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }
}