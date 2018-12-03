package com.example.ps_4.sudoku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_statistics.*
import java.io.File

class StatisticsActivity : AppCompatActivity() , ISetBackground{

    val TAG = "Statistics"

    lateinit var stat : Statistic.companion
    lateinit var readWritestat : Statistic.companion.ReadWrite

    override fun onCreate(savedInstanceState : Bundle? ) {
        Log.d(TAG , "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        setBackground()

        val patheasy = File(filesDir.toString() + "/" + "easystat").toString()
        val pathnormal = File(filesDir.toString() + "/" + "normalstat").toString()
        val pathhard = File(filesDir.toString() + "/" + "hardstat").toString()
        val pathinsane = File(filesDir.toString() + "/" + "insanestat").toString()

        stat = Statistic.companion

        readWritestat = Statistic.companion.ReadWrite(patheasy , pathnormal , pathhard , pathinsane)

        bt_easy.setOnClickListener{
            showEasy()
        }

        bt_normal.setOnClickListener{
            showNormal()
        }

        bt_hard.setOnClickListener{
            showHard()
        }

        bt_insane.setOnClickListener{
            showInsane()
        }

        bt_reset_stat.setOnClickListener {
            stat.clearStat(stat.currentdiff)
            stat.show(stat.currentdiff , handlestattextview)
        }

        readWritestat.read()

        showEasy()
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
        // write to file all stat
        readWritestat.write()
    }

    override fun onDestroy() {
        Log.d(TAG , "onDestroy")
        super.onDestroy()
    }

    override fun setBackground() {
        main_layout_statistic.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        statistics_layout.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_easy.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_normal.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_hard.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_insane.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        handlestattextview.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_reset_stat.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
    }


    private fun showEasy(){
        stat.show(stat.easy , handlestattextview)
        stat.currentdiff = stat.easy
    }

    private fun showNormal(){
        stat.show(stat.normal, handlestattextview)
        stat.currentdiff = stat.normal
    }

    private fun showHard(){
        stat.show(stat.hard, handlestattextview)
        stat.currentdiff = stat.hard
    }

    private fun showInsane(){
        stat.show(stat.insane, handlestattextview)
        stat.currentdiff = stat.insane
    }

}