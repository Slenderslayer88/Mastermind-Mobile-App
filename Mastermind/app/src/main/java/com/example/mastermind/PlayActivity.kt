package com.example.mastermind

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity


enum class PegColor(val code: Int) {
    Red(0),
    Blue(1),
    Green(2),
    Purple(3),
    White(4),
    Orange(5),
}

var selectedColor: PegColor = PegColor.White
val secretCode = listOf(PegColor.Red, PegColor.Blue, PegColor.Green, PegColor.Purple)
var pastGuesses: ArrayList<Int> = ArrayList()

class PlayActivity : ComponentActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_activity)

        val guessesAllowed = 6
        var currentGuess: Int = 1
        val checkGuessButton = findViewById<Button>(R.id.check_guess_btn)
        val guessCountTxt = findViewById<TextView>(R.id.guess_count_txt)
        guessCountTxt.text = guessesAllowed.toString()

        val fm = fragmentManager
        var currentGuessRow = fm.findFragmentById(R.id.guess_row_fragment) as GuessRowFragment
        val guessRowFragmentContainer = findViewById<LinearLayout>(R.id.guess_row_fragment_container)

        checkGuessButton.setOnClickListener {
            currentGuess += 1

            currentGuessRow.setFeedbackNodes(resources)
            currentGuessRow.disableGuessRow()

            if(currentGuessRow.currentGuessColors == secretCode){
                guessCountTxt.text = "You Won!"
                guessCountTxt.textSize = 30f
                checkGuessButton.setOnClickListener(null)
                return@setOnClickListener
            } else if(currentGuess > guessesAllowed){
                guessCountTxt.text = "You Lost!"
                guessCountTxt.textSize = 30f
                checkGuessButton.setOnClickListener(null)
                return@setOnClickListener
            }

            guessCountTxt.text = (guessesAllowed - (currentGuess - 1)).toString()

            val fragTransaction: FragmentTransaction = fm.beginTransaction()
            val newGuessRowFragment: Fragment = GuessRowFragment()
            fragTransaction.add(guessRowFragmentContainer.id, newGuessRowFragment, "fragment$currentGuess")
            fragTransaction.commit()

            currentGuessRow = newGuessRowFragment as GuessRowFragment
        }

        val menuBtn = findViewById<Button>(R.id.menuBtn)
        menuBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val optionsBtn = findViewById<Button>(R.id.options_btn)
        optionsBtn.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val newGameBtn = findViewById<Button>(R.id.newGameBtn)
        newGameBtn.setOnClickListener {
            val switchActivityIntent = Intent(this, PlayActivity::class.java)
            startActivity(switchActivityIntent)
        }

        val pegBtns: ArrayList<Button> = ArrayList()
        pegBtns.add(findViewById(R.id.red_peg_btn))
        pegBtns.add(findViewById(R.id.blue_peg_btn))
        pegBtns.add(findViewById(R.id.green_peg_btn))
        pegBtns.add(findViewById(R.id.purple_peg_btn))
        pegBtns.add(findViewById(R.id.orange_peg_btn))
        pegBtns.add(findViewById(R.id.white_peg_btn))

        for ((index, pegBtn: Button) in pegBtns.withIndex()){
            pegBtn.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            selectedColor = when(index){
                                0 -> PegColor.Red
                                1 -> PegColor.Blue
                                2 -> PegColor.Green
                                3 -> PegColor.Purple
                                4 -> PegColor.Orange
                                5 -> PegColor.White
                                else -> PegColor.Red
                            }
                            val myShadow = DragShadowBuilder(v)
                            v?.startDragAndDrop(null, myShadow, v, 0)
                        }
                    }

                    return v?.onTouchEvent(event) ?: true
                }
            })
        }

        var hintsRemaining = 3
        val hintBtn = findViewById<Button>(R.id.hint_btn)
        hintBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            if(hintsRemaining > 0){
                builder.setTitle(Html.fromHtml("<font color='#b9c1c7'>Are you sure you want a hint? (hints remaining: $hintsRemaining)</font>"))
                    .setPositiveButton("Yes!") { dialog, id ->
                        hintsRemaining -= 1
                        currentGuessRow.usedHint( 3 - hintsRemaining, resources)
                    }
                    .setNegativeButton("No") { dialog, id -> }
            } else {
                builder.setTitle(Html.fromHtml("<font color='#b9c1c7'>No hints remaining</font>"))
                    .setPositiveButton("ok") { dialog, id -> }
            }
            val hintAlert = builder.create()
            hintAlert.show()
        }
    }
}



