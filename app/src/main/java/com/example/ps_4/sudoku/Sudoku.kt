package com.example.ps_4.sudoku


import java.util.*
import java.util.EnumSet.range


class Sudoku(cellsfromfieldcreator: Array<Array<Cell>>)  {
    //link on a field
    private var cells : Array<Array<Cell>>
    //value of cells
    val sudoku : Array<IntArray> =
            Array(9 , { IntArray(9 , {0})})
    //help for genearator
    val generator : SudokuGenerator
    private val arrowcol : AreaRowColumnGetter

    init {
        generator = SudokuGenerator()
        cells = cellsfromfieldcreator
        arrowcol = AreaRowColumnGetter(sudoku)
        reestabilish()
        //all actions after that
    }

    private fun reestabilish(){
        //basefield
        sudoku[0] = intArrayOf(1,2,3,4,5,6,7,8,9)
        sudoku[1] = intArrayOf(4,5,6,7,8,9,1,2,3)
        sudoku[2] = intArrayOf(7,8,9,1,2,3,4,5,6)
        sudoku[3] = intArrayOf(2,3,4,5,6,7,8,9,1)
        sudoku[4] = intArrayOf(5,6,7,8,9,1,2,3,4)
        sudoku[5] = intArrayOf(8,9,1,2,3,4,5,6,7)
        sudoku[6] = intArrayOf(3,4,5,6,7,8,9,1,2)
        sudoku[7] = intArrayOf(6,7,8,9,1,2,3,4,5)
        sudoku[8] = intArrayOf(9,1,2,3,4,5,6,7,8)
    }

    fun clearField(){
        for (i in 0..8)
            for (k in 0..8)
                cells[i][k].cell.text = ""
    }

    inner class SudokuGenerator  {

        fun mix(amount : Int){
            reestabilish()
            val funcs:Array<()->Unit>  = arrayOf({transponding()} , {swapRowsSmall()},
                    {swapRowsArea()},{swapColumnArea()},{swapColumnsSmall()})

            for (i in 0..amount){
                funcs[Random().nextInt(funcs.size ) + 0]()
            }
        }

       private fun transponding(){
            val help : ArrayList<ArrayList<Int>> = ArrayList(9)
            var d = 0
            for (i in 0..2)
                for (k in 0..2) {
                    help.add(ArrayList())
                    help[d++].addAll(arrowcol.getcolumnInt(i, k))
                }
            var x1 = 0
            var x2 = 2
            var y1 = 0
            var y2 = 2
            var inc = 0
            for ( l in 0..2) {
                for (h in 0..2) {
                    d = 0
                    for (i in x1..x2) {
                        for (k in y1..y2) {
                            sudoku[i][k] = help[inc][d++]
                        }
                    }
                    inc++
                    y1 += 3
                    y2 += 3
                }
                y1 = 0
                y2 = 2
                x1 += 3
                x2 += 3
            }
        }

       private fun swapRowsSmall(region : Int = (Random().nextInt(3) + 0 ) ) {

            val row1: ArrayList<Int> = ArrayList(9)
            val row2: ArrayList<Int> = ArrayList(9)
            row1.addAll(arrowcol.getrowInt((region * 3) +  0 , 0 ))
            row2.addAll(arrowcol.getrowInt((region * 3) +  0 , 6 ))

            var d = 0
            for (i in (0 + region * 3)..(region * 3 + 2))
                for (k in 0..2) {
                    sudoku[i][k] = row2[d]
                    sudoku[i][k + 6] = row1[d++]
                }
       }

       private fun swapColumnsSmall(region : Int = (Random().nextInt(3) + 0 )) {
            val col1: ArrayList<Int> = ArrayList(9)
            val col2: ArrayList<Int> = ArrayList(9)
            col1.addAll(arrowcol.getcolumnInt(region, 0))
            col2.addAll(arrowcol.getcolumnInt(region, 2))
            var d = 0
            for (i in 0..2)
                for (k in 0..2) {
                    sudoku[(i * 3) + region][(k * 3)] = col2[d]
                    sudoku[(i * 3) + region][ 2 + (k * 3)] = col1[d++]
                }
       }

       private fun swapRowsArea(area1 : Int = (Random().nextInt(3) + 0 ),
            b : Int = (Random().nextInt(3) + 0 )){
            var area2 = b
            while (area1 == area2) {
                area2 = (Random().nextInt(3) + 0)
            }

            val rowarea1 : ArrayList<ArrayList<Int>> = ArrayList( 3 )
            val rowarea2 : ArrayList<ArrayList<Int>> = ArrayList( 3 )
            //I should change this code
            for (i in 0..2){
                rowarea1.add(ArrayList())
                rowarea1[i].addAll(arrowcol.getrowInt((area1 * 3) , (i * 3)))
                rowarea2.add(ArrayList())
                rowarea2[i].addAll(arrowcol.getrowInt((area2 * 3) , (i * 3)))
            }

            var d = 0
            for (i in 0..2) {
                for (k in 0..2) {
                    for (j in 0..2) {
                        sudoku[(area2 * 3) + k][j + (i * 3)] = rowarea1[i][d]
                        sudoku[(area1 * 3) + k][j + (i * 3)] = rowarea2[i][d++]
                    }
                }
                d=0
            }
       }

       private  fun swapColumnArea(area1: Int = (Random().nextInt(3) + 0 ), area2: Int = (Random().nextInt(3) + 0 )){
            transponding()
            swapRowsArea(area1 ,area2)
            transponding()
       }
    }
}



