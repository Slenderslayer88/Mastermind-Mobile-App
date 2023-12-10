package com.example.mastermind

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity


class OptionsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.options_activity)

        val howToPlayBtn = findViewById<Button>(R.id.how_to_play_btn)

        howToPlayBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setMessage(Html.fromHtml("<font color='#b9c1c7'>Your goal is to successfully guess a secret code of colored pegs. Each time you create an arrangement of colored pegs and hit 'guess' the game will " +
                    "give you feedback on how close your guess was. After you make your guess, to the right of the row different, smaller, pegs will light up. For each black peg you got a color right but in " +
                    "the wrong spot. For each gold peg, you got a peg in the right spot and color. Using this feedback you can continue to make better and better guesses until you crack the code. Ready for the challenge? </font>"))
                .setPositiveButton("Okay") { dialog, id -> }

            val hintAlert = builder.create()
            hintAlert.show()
        }

        val volumePercentage = findViewById<TextView>(R.id.volume_percentage)
        val volumeSlider = findViewById<SeekBar>(R.id.volume_slider)
        volumeSlider.max = 100
        volumeSlider.progress = 100
        volumeSlider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                volumePercentage.text = buildString {
                    append(progress.toString())
                    append("%")
                    BackgroundMusicService.mediaPlayer.setVolume(progress.toFloat() / 100,progress.toFloat() / 100)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val playBtn = findViewById<Button>(R.id.play_btn)
        playBtn.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val menuBtn = findViewById<Button>(R.id.menu_btn)
        menuBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val exit = findViewById<Button>(R.id.exit_btn)
        exit.setOnClickListener {
            BackgroundMusicService.mediaPlayer.stop()
            finishAffinity()
            System.exit(0)
        }

    }


}