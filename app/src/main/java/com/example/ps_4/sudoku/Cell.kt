package com.example.ps_4.sudoku

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.text.Layout
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlin.reflect.KProperty0

class Cell( param : TextView , x :Int , y :Int) {
    val cell : TextView
    private  val filling = android.support.v7.widget.GridLayout.LayoutParams(android.support.v7.widget.GridLayout.spec(
            android.support.v7.widget.GridLayout.UNDEFINED, android.support.v7.widget.GridLayout.FILL, 1f),
            android.support.v7.widget.GridLayout.spec(android.support.v7.widget.GridLayout.UNDEFINED, android.support.v7.widget.GridLayout.FILL, 1f))

    var xLocation : Int
    var yLocation : Int

    var flagPen = false
    var ifill = false

    init {
        xLocation = x
        yLocation = y
        cell = param
        cell.setBackgroundResource(R.drawable.bordercell)
        cell.gravity = Gravity.CENTER
        cell.setTextSize(30.0F)
        cell.layoutParams = filling
        cell.setTextColor(Color.DKGRAY)
        //for that the cells are of the same size
        cell.minWidth = 100
        cell.minHeight = 100
    }

    fun penModeActivated(){
        cell.setTextSize(10.0F)
        flagPen = true
    }
    fun penModeDeactivated(){
        cell.setTextSize(30.0F)
        flagPen = false
    }
}

