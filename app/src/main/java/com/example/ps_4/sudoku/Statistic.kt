package com.example.ps_4.sudoku

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_statistics.view.*
import java.io.File

class Statistic{
    val TAG  = "Statistic"

    object companion{
        val easy = Stat()
        val normal = Stat()
        val hard = Stat()
        val insane = Stat()


        var currentdiff  = easy

        var curentseriesofvictory = 0

        val besttimedefault = 1000000000000L

        class Stat{
            var gamesplayed : Int = 0
            var besttime : Long = besttimedefault
            var averagetime : Long = 0L
            var bestseriesofvictories = 0
            var gamedefeat = 0
            var gamewithwin = 0
        }

        fun clearStat(a : Stat){
            a.gamesplayed = 0
            a.gamewithwin = 0
            a.averagetime = 0L
            a.besttime = besttimedefault
            a.bestseriesofvictories = 0
            a.gamedefeat = 0
        }

        fun timeParser(a : Long) : String{
            if (a != besttimedefault) {
                var b = a
                b *= -(1)
                return ((b / 60).toString() + ":" + (b % 60))
            }
            else
                return "0"
        }

        fun show(a : Stat , b : LinearLayout){
            b.game_played.text = "Game Played :   " + a.gamesplayed.toString()
            b.best_time.text = "Best Time :   " + timeParser(a.besttime) + "    minutes"
            var c = a.averagetime.toString()
            b.avgtime.text =  "Average Time :   " + timeParser(a.averagetime) +  "    minutes"
            c = a.averagetime.toString()
            b.bestseriesofwin.text =  "Best series of Victory :   " + a.bestseriesofvictories.toString()
            b.game_defeat.text =  "Game Lost :   "  + a.gamedefeat.toString()
        }

        class ReadWrite(patheasy : String ,pathnormal : String, pathhard : String, pathinsane : String ) : IReadWrite{

            val allstatpath : ArrayList<Pair<String , companion.Stat>> = ArrayList( 4  )

            var text : StringBuilder = StringBuilder()

            init {
                allstatpath.add(Pair(patheasy , easy))
                allstatpath.add(Pair(pathnormal , normal))
                allstatpath.add(Pair(pathhard , hard))
                allstatpath.add(Pair(pathinsane , insane))
            }

            override fun read() : Boolean {
                var flag  = true

                allstatpath.forEach {
                    if (checkFile(it.first)) {
                        try {
                            val stat = StringBuilder(File(it.first).readText()).toString().split('@')
                            it.second.gamesplayed = stat[0].toInt()
                            it.second.besttime = stat[1].toLong()
                            it.second.averagetime = stat[2].toLong()
                            it.second.bestseriesofvictories = stat[3].toInt()
                            it.second.gamedefeat = stat[4].toInt()
                        } catch (e: Exception) {
                            flag = false
                        }
                    }
                }
                return flag
            }

            override fun write() {
                allstatpath.forEach { writeStat(it.first ,it.second ) }
            }

            fun writeStat(path : String , statdiff : Statistic.companion.Stat ){

                text.append(statdiff.gamesplayed.toString() + "@")
                text.append(statdiff.besttime.toString() + "@")
                text.append(statdiff.averagetime.toString() + "@")
                text.append(statdiff.bestseriesofvictories.toString() + "@")
                text.append(statdiff.gamedefeat.toString() + "@")
                try {
                    File(path).writeText(text = text.toString())
                } catch (e: Exception) {
                    Log.d(TAG , "except $statdiff")
                }
                text.setLength(0)
            }

            private fun checkFile(path : String): Boolean {
                var result = false
                val fileCheck = File(path)
                if (fileCheck.exists())
                    result = true
                return result
            }

            override fun clear() {
                allstatpath.forEach { File(it.first).writeText(text = "") }
            }
        }
    }
}