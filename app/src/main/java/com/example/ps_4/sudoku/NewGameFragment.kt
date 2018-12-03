package com.example.ps_4.sudoku

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewCompat.setBackground
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.fragment_new_game.*
import java.util.*

class NewGameFragment : Fragment() ,ISetBackground {
    val TAG = "FragmentNewGame"

    lateinit var activityGame : INewGameFragmentListener

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)

        try {
            activityGame = context  as INewGameFragmentListener
        }
        catch (e : ClassCastException){
            Toast.makeText(this.activity , TAG +  "  " + e , Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        return inflater!!.inflate(R.layout.fragment_new_game , container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackground()

        bt_fragment_easy.setOnClickListener {
            activityGame.newGame(Random().nextInt(60 - 46 ) + 46)
        }

        bt_fragment_normal.setOnClickListener {
            activityGame.newGame(Random().nextInt( 46 - 36 ) + 36)
        }

        bt_fragment_hard.setOnClickListener {
            activityGame.newGame(Random().nextInt(36 - 26 ) + 26)
        }

        bt_fragment_insane.setOnClickListener {
            activityGame.newGame(Random().nextInt(26 - 20) + 20)
        }

        bt_fragment_reset.setOnClickListener {
            activityGame.startTheGameAgain()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
    }

    override fun setBackground(){
        main_fragment_new_game_layout.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        textView2.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_fragment_easy.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_fragment_normal.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_fragment_hard.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_fragment_insane.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_fragment_reset.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
    }
}