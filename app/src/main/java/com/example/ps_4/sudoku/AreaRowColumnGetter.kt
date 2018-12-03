package com.example.ps_4.sudoku

import kotlin.collections.ArrayList

class AreaRowColumnGetter{
    constructor(a : Array<Array<Cell>>){
        cells = a
        areaCells =  ArrayList(9)
    }
    constructor(b : Array<IntArray> ){
        sudoku = b
        areaInt = ArrayList(9)
    }

    private var areaCells: ArrayList<Cell> = ArrayList(0)
    private var areaInt: ArrayList<Int> = ArrayList(0)
    private var cells: Array<Array<Cell>>? = null
    private var sudoku : Array<IntArray>? = null

    fun getrowInt(x: Int , y: Int) : ArrayList<Int>{
        areaInt.clear()
        getRow(x, y)
        return areaInt
    }

    fun getcolumnInt(x: Int , y: Int) : ArrayList<Int>{
        areaInt.clear()
        getColumn(x, y)
        return areaInt
    }

    fun getrowCell(x: Int , y: Int) : ArrayList<Cell>{
        areaCells.clear()
        getRow(x, y)
        return areaCells
    }

    fun getcolumnCell(x: Int , y: Int) : ArrayList<Cell>{
        areaCells.clear()
        getColumn(x, y)
        return areaCells
    }

    fun getareaCell(x: Int) : ArrayList<Cell>{
        areaCells.clear()
        getArea(x)
        return areaCells
    }

    private fun getRow(x: Int, y: Int )  {
        when ((x <= 2) and (y <= 2)) {
            true -> fillrowCells(0, 2, 0, 2) }
        when ((x <= 2) and (y <= 5) and (y >= 3)) {
            true -> fillrowCells(0, 2, 3, 5) }
        when ((x <= 2) and (y <= 8) and (y >= 6)) {
            true -> fillrowCells(0, 2, 6, 8) }
        when ((x >= 3) and (x <= 5) and (y <= 2)){
            true -> fillrowCells(3, 5, 0, 2)}
        when ((x >= 3) and (x <= 5) and (y >= 3) and (y <= 5)){
            true ->fillrowCells(3, 5, 3, 5)}
        when ((x >= 3) and (x <= 5) and (y <= 8) and (y >= 6)){
            true ->fillrowCells(3, 5, 6, 8)}
        when ((x >= 6) and (x <= 8) and (y <= 2)){
            true -> fillrowCells(6, 8, 0, 2)}
        when ((x >= 6) and (x <= 8) and (y >= 3) and (y <= 5)){
            true -> fillrowCells(6, 8, 3, 5)}
        when ((x >= 6) and (x <= 8) and (y <= 8) and (y >= 6)){
            true -> fillrowCells(6, 8, 6, 8)}
    }

    private fun fillrowCells(x1: Int, x2: Int, y1: Int, y2: Int) {
        for (i in x1..x2) {
            for (k in y1..y2) {
                if (cells != null)
                    areaCells.add(cells!![i][k])
                if (sudoku != null)
                    areaInt.add(sudoku!![i][k])
            }
        }
    }

    private fun getColumn(x: Int, y: Int) {
        when ((x == 0) and (y == 3 || y == 6 || y == 0) || (x == 6) and (y == 3 || y == 6 || y == 0) || (x == 3) and (y == 3 || y == 6 || y == 0)){
            true -> fillcolumncells(0, 0)}
        when ((x == 0) and (y == 1 || y == 4 || y == 7) || (x == 6) and (y == 1 || y == 4 || y == 7) || (x == 3) and (y == 1 || y == 4 || y == 7)){
            true -> fillcolumncells(0, 1)}
        when ((x == 0) and (y == 2 || y == 5 || y == 8) || (x == 6) and (y == 2 || y == 5 || y == 8) || (x == 3) and (y == 2 || y == 5 || y == 8)){
            true -> fillcolumncells(0, 2)}
        when ((x == 1) and (y == 3 || y == 6 || y == 0) || (x == 4) and (y == 3 || y == 6 || y == 0) || (x == 7) and (y == 3 || y == 6 || y == 0)){
            true -> fillcolumncells(1, 0)}
        when ((x == 1) and (y == 1 || y == 4 || y == 7) || (x == 4) and (y == 1 || y == 4 || y == 7) || (x == 7) and (y == 1 || y == 4 || y == 7)){
            true ->fillcolumncells(1, 1)}
        when ((x == 1) and (y == 2 || y == 5 || y == 8) || (x == 4) and (y == 2 || y == 5 || y == 8) || (x == 7) and (y == 2 || y == 5 || y == 8)){
            true -> fillcolumncells(1, 2)}
        when ((x == 2) and (y == 3 || y == 6 || y == 0) || (x == 5) and (y == 3 || y == 6 || y == 0) || (x == 8) and (y == 3 || y == 6 || y == 0)){
            true -> fillcolumncells(2, 0)}
        when ((x == 2) and (y == 1 || y == 4 || y == 7) || (x == 5) and (y == 1 || y == 4 || y == 7) || (x == 8) and (y == 1 || y == 4 || y == 7)){
            true ->fillcolumncells(2, 1)}
        when ((x == 2) and (y == 2 || y == 5 || y == 8) || (x == 5) and (y == 2 || y == 5 || y == 8) || (x == 8) and (y == 2 || y == 5 || y == 8)){
            true ->fillcolumncells(2, 2)}
    }

    private fun fillcolumncells(x: Int, y: Int) {
        for (i in 0..2)
            for (k in 0..2) {
                if (cells != null)
                    areaCells.add(cells!![x + (i * 3)][y + (k * 3)])
                if (sudoku != null)
                    areaInt.add(sudoku!![x + (i * 3)][y + (k * 3)])
            }

    }

    private fun getArea(x: Int){
        for (i in 0..8) {
            if (cells != null)
                areaCells.add(cells!![x][i])
            if (sudoku != null)
                areaInt.add(sudoku!![x][i])
        }
    }
}