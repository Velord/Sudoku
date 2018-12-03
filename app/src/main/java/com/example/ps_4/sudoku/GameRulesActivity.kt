package com.example.ps_4.sudoku

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.ps_4.sudoku.R.id.*
import kotlinx.android.synthetic.main.activity_gamerules.*
import kotlinx.android.synthetic.main.activity_gamerules.view.*
import kotlinx.android.synthetic.main.activity_statistics.*
import java.io.File

class GameRulesActivity : AppCompatActivity() , ISetBackground {

    val TAG = "GameRules"

    override fun onCreate(savedInstanceState : Bundle? ) {
        Log.d(TAG , "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamerules)

        setBackground()

        bt_skip.setOnClickListener{
            startGameAcitivity()
        }

        val funcs : Array<()->Unit> = arrayOf({secondGameRules()} , {thirdGameRules()} , {startGameAcitivity()})
        var iter  = 0

        bt_next.setOnClickListener {
            funcs[iter++]()
        }

    }

    override fun onStart() {
        Log.d(TAG , "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG , "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG , "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG , "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG , "onDestroy")
        super.onDestroy()
    }

    override fun setBackground(){
        game_rules_container.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_next.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_skip.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        text_game_rules.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
    }

    private fun secondGameRules(){
        game_rules_container.removeView(text_game_rules)
        imageView.setImageResource(R.drawable.game_rules_second)
    }

    private fun thirdGameRules(){
        imageView.setImageResource(R.drawable.game_rules_third)
        bt_next.text = "OK"
    }

    private fun startGameAcitivity(){
        val intent = Intent(this , GameActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}