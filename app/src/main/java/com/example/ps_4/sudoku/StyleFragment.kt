package com.example.ps_4.sudoku

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_style.*
import java.security.AccessController.getContext


class StyleFragment : Fragment() , ISetBackground {

    private val TAG = "FragmentStyle"

    private var currentstyle : ImageButton?  = null

    private lateinit var activityGame : IStyleFragmentListener

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)

        try {
            activityGame = context  as IStyleFragmentListener
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
        return inflater!!.inflate(R.layout.fragment_style , container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackground()
        if (StyleChanger.companion.currentstyle == "white") {
            currentstyle = bt_white
            currentstyle!!.setImageResource(R.drawable.whitebig)
        }
        else if (StyleChanger.companion.currentstyle == "yellow") {
            currentstyle = bt_yellow
            currentstyle!!.setImageResource(R.drawable.yellowbig)
        }
        else if (StyleChanger.companion.currentstyle == "dark") {
            currentstyle = bt_dark
            currentstyle!!.setImageResource(R.drawable.darkbig)
        }




        bt_white.setOnClickListener {
            if (currentstyle != bt_white) {
                if (currentstyle == bt_yellow) {
                    currentstyle!!.setImageResource(R.drawable.yellowsmall)
                    currentstyle = bt_white
                    currentstyle!!.setImageResource(R.drawable.whitebig)
                }
                else{
                    currentstyle!!.setImageResource(R.drawable.darksmall)
                    currentstyle = bt_white
                    currentstyle!!.setImageResource(R.drawable.whitebig)
                }
            }

            StyleChanger.companion.whiteStyle(resources)
            setBackground()
            activityGame.refreshView()
        }

        bt_yellow.setOnClickListener {
            if (currentstyle != bt_yellow) {
                if (currentstyle == bt_white) {
                    currentstyle!!.setImageResource(R.drawable.whitesmall)
                    currentstyle = bt_yellow
                    currentstyle!!.setImageResource(R.drawable.yellowbig)
                }
                else {
                    currentstyle!!.setImageResource(R.drawable.darksmall)
                    currentstyle = bt_yellow
                    currentstyle!!.setImageResource(R.drawable.yellowbig)
                }
            }

            StyleChanger.companion.yellowStyle(resources)
            setBackground()
            activityGame.refreshView()
        }

        bt_dark.setOnClickListener {
            if (currentstyle != bt_dark) {
                if (currentstyle == bt_yellow) {
                    currentstyle!!.setImageResource(R.drawable.yellowsmall)
                    currentstyle = bt_dark
                    currentstyle!!.setImageResource(R.drawable.darkbig)
                }
                else {
                    currentstyle!!.setImageResource(R.drawable.whitesmall)
                    currentstyle = bt_dark
                    currentstyle!!.setImageResource(R.drawable.darkbig)
                }
            }

            StyleChanger.companion.darkStyle(resources)
            setBackground()
            activityGame.refreshView()
        }

        bt_OK.setOnClickListener {
            activityGame.hidestylefragment()
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

    override fun setBackground() {
        fragment_style_main_layout.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        textView3.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_OK.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
    }
}
