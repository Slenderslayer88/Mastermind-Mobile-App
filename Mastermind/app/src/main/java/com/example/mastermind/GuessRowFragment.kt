package com.example.mastermind

import android.app.Fragment
import android.content.res.Resources
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

class GuessRowFragment: Fragment() {

    //var selectedColor: PegColor = PegColor.White
    public val currentGuessColors = mutableListOf(PegColor.White, PegColor.White, PegColor.White, PegColor.White)

    val pegBtns: ArrayList<ImageView> = ArrayList()
    val feedbackNodes: ArrayList<ImageView> = ArrayList()
    var myLayout: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.guess_row_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myLayout = view?.findViewById(R.id.myLayout)

        for(pegBtnId: Int in listOf<Int>(R.id.peg_btn_1, R.id.peg_btn_2, R.id.peg_btn_3, R.id.peg_btn_4)){
            pegBtns.add(view?.findViewById(pegBtnId) as ImageView)
        }

        for((index, pegBtn: ImageView) in pegBtns.withIndex()){
            pegBtn.setOnDragListener { view: View, dragEvent: DragEvent ->

                when (dragEvent.action) {
                    DragEvent.ACTION_DROP -> {
                        pegBtn.background = view?.resources?.getDrawable(setPegBtnDrawable(selectedColor))
                        currentGuessColors[index] = selectedColor
                    }
                }

                return@setOnDragListener true
            }
        }


        for(feedbackNodeId: Int in listOf<Int>(R.id.feedback_node_1, R.id.feedback_node_2, R.id.feedback_node_3, R.id.feedback_node_4)){
            feedbackNodes.add(view?.findViewById(feedbackNodeId) as ImageView)
        }
    }

    public fun disableGuessRow(){
        for (pegBtn: ImageView in pegBtns){
            pegBtn.setOnClickListener(null)
        }
    }

    public fun setFeedbackNodes(drawableResource: Resources){
        var feedbackMap: Int = 0
        for(i: Int in IntRange(0, currentGuessColors.size - 1)){
            if(currentGuessColors[i] == secretCode[i]){
                feedbackMap += 10
            } else if(currentGuessColors.contains(secretCode[i])) {
                feedbackMap += 1
            }
        }
        for(feedbackNode: ImageView in feedbackNodes){
            if(feedbackMap >= 10){
                feedbackNode.background = drawableResource.getDrawable(R.drawable.feedback_peg_correct_place)
                feedbackMap -= 10
            } else if (feedbackMap >= 1){
                feedbackNode.background = drawableResource.getDrawable(R.drawable.feedback_peg_correct_color)
                feedbackMap -= 1
            }
        }
    }

    private fun setPegBtnDrawable(selectedColor: PegColor): Int{
        when (selectedColor){
            PegColor.Red -> return R.drawable.peg_btn_red
            PegColor.Blue -> return R.drawable.peg_btn_blue
            PegColor.Green -> return R.drawable.peg_btn_green
            PegColor.Purple -> return R.drawable.peg_btn_purple
            PegColor.White -> return R.drawable.peg_btn_white
            PegColor.Orange -> return R.drawable.peg_btn_orange
            else -> return R.drawable.peg_btn_white
        }
    }

    private fun setPegBtnDrawableHint(selectedColor: PegColor): Int{
        when (selectedColor){
            PegColor.Red -> return R.drawable.peg_btn_red_hint
            PegColor.Blue -> return R.drawable.peg_btn_blue_hint
            PegColor.Green -> return R.drawable.peg_btn_green_hint
            PegColor.Purple -> return R.drawable.peg_btn_purple_hint
            PegColor.White -> return R.drawable.peg_btn_white_hint
            PegColor.Orange -> return R.drawable.peg_btn_orange_hint
            else -> return R.drawable.peg_btn_white_hint
        }
    }

    public fun usedHint(hintNum: Int, drawableResource: Resources){
        for (i: Int in IntRange(0, hintNum - 1)){
            pegBtns[i].background = drawableResource.getDrawable(setPegBtnDrawableHint(secretCode[i]))
            currentGuessColors[i] = secretCode[i]
            pegBtns[i].setOnDragListener { view: View, dragEvent: DragEvent -> return@setOnDragListener false }
        }


    }
}