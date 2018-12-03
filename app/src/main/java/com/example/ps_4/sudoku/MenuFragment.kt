package com.example.ps_4.sudoku

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : android.support.v4.app.Fragment() {

    val TAG = "FragmentMenu"

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        return inflater!!.inflate(R.layout.fragment_menu , container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackground()

        bt_statistics.setOnClickListener {
            Log.d(TAG,"launch Statistics")
            val intent  = Intent(this.activity , StatisticsActivity::class.java)
            startActivity(intent)
        }

        bt_rules.setOnClickListener {
            Log.d(TAG , "Launch Game Rules")
            val intent  = Intent(this.activity , GameRulesActivity::class.java)
            startActivity(intent)
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

    fun setBackground(){
        var a = StyleChanger.companion.backgroundgame!!
        print("dfds")
        print("sdfsd")

        menucontainer.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_statistics.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_settings.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_Help.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_rules.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
    }
}
