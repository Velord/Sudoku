package com.example.ps_4.sudoku

import android.graphics.Color
import java.util.*
import kotlin.collections.ArrayList


class UserCells (cellsfromfieldcreator: Array<Array<Cell>>) {
    //link on a field
    private var cells: Array<Array<Cell>>
    //value for cells
    private var solver : Array<IntArray>  = Array(9 , { IntArray(9 , {0} ) })
    //transit collection for sudoku
    private var solvercoord : ArrayList<Pair<Int, Int>> = ArrayList(81 )
    //not mutable
    var notmutable: ArrayList<Cell> = ArrayList(20)
    //copy not mutable
    val copynotmutable : ArrayList<Pair<Pair<Int , Int> , String>> = ArrayList(20)
    //checker handler
    val check : Checker = Checker()
    //help for checker
    private val arrowcol : AreaRowColumnGetter

    init {
        cells = cellsfromfieldcreator
        arrowcol = AreaRowColumnGetter(cells)
    }

    private fun setRandomCellField(count : Int){
        notmutable.clear()
        copynotmutable.clear()

        var amount = count
        var rand1: Int

        while (amount > 0) {
            val a  = solvercoord.size
            rand1 = Random().nextInt((a) + 0)

            val coord = solvercoord[rand1]

            notmutable.add(cells[coord.first][coord.second])

            val index = notmutable.lastIndex

            notmutable[index].cell.text =
                    solver[coord.first][coord.second].toString()

            copyNotMutableCells()

            solvercoord.remove(coord)
            amount--
        }
    }

    fun setField(count: Int){
        setRandomCellField(count)
        setFieldFromNotMutableCells()
    }

    fun copyNotMutableCells(){
        notmutable.forEach { copynotmutable.add(Pair(Pair( it.xLocation , it.yLocation ),
                it.cell.text.toString())) }
    }

    fun setFieldFromNotMutableCells(){
        copynotmutable.forEach { cells[it.first.first][it.first.second].cell.text = it.second }
    }

    fun copy(a: Array<IntArray>) {
        for (i in 0..8)
            for (k in 0..8) {
                solver[i][k] = a[i][k]
                solvercoord.add(Pair(i,k))
            }
    }

    inner class Checker {
        private val sameValueBackground: ArrayList<Cell> = ArrayList(9)
        private val sameColorText: ArrayList<Cell> = ArrayList(3)
        private val sameValueText : ArrayList<Cell> = ArrayList(9)
        private val cellforCheck: ArrayList<Cell> = ArrayList(21)
        private var areaCells: ArrayList<Cell> = ArrayList(21)

        fun clean(){
            clearsameColorText()
            clearSameValueBackground()
            clearCellForCheckBackground()
            clearSameValueTextBackgroundInOtherField()
        }
        private fun addAreaCells(x: Int , y : Int) {
            areaCells.addAll(arrowcol.getareaCell(x))
            areaCells.addAll(arrowcol.getrowCell(x,y))
            areaCells.addAll(arrowcol.getcolumnCell(x,y))
            //remove same elements
            areaCells = ArrayList(HashSet<Cell>(areaCells))
            //remove self
            areaCells.remove(cells[x][y])
        }

        fun removeCellFromCollection(x: Int, y : Int){
            sameColorText.remove(cells[x][y])
            sameValueBackground.remove(cells[x][y])
        }

        fun checkCell(x: Int, y: Int) {
            if (areaCells.size != 0)
                areaCells.clear()

            if (cellforCheck.size != 0)
                clearCellForCheckBackground()

            if (sameValueBackground.size != 0)
                clearSameValueBackground()

            if (sameValueText.size != 0)
                clearSameValueTextBackgroundInOtherField()

            //add all cells for current cell
            addAreaCells(x, y)

            //set background for check
            areaCells.forEach {
                it.cell.setBackground(StyleChanger.companion.bordercellforcheck)
                cellforCheck.add(it)
            }
            //if not penmode
            if (cells[x][y].flagPen == false) {
                //проверка выбранной ячейки на те же значения в поле и установка бекграунла и текста в соответствующие значения и добавления их в массивы с данными для дальнейшей обработки
                for (it in areaCells) {
                    //if not blank
                    if (((cells[x][y].cell.text != "") and (it.cell.text != "") //if not string in format 1  2  3 etc.
                                    and (cells[x][y].cell.text.length == 1) and  (it.cell.text.length == 1) )) {
                        if (cells[x][y].cell.text.toString().toInt() == it.cell.text.toString().toInt()) {
                            //если содержится в неизменяемом , то не изменять значение
                            if (!(notmutable.contains(it)))
                                it.cell.setTextColor(Color.RED)
                            //бекграунд установить в любом случае
                            it.cell.setBackground(StyleChanger.companion.bordercellsamevalue)
                            //то же самое только для выбранной ячейки , но для нее бекграунд изменять не надо
                            if (!(notmutable.contains(cells[x][y]))) {
                                cells[x][y].cell.setTextColor(Color.RED)
                                sameColorText.add(cells[x][y])
                            }
                            sameValueBackground.add(it)

                            if (!(sameColorText.contains(it)))
                                sameColorText.add(it)
                        }
                    }
                }
                setSameValueTextOtherField(x,y)
            }
        }

        private fun setSameValueTextOtherField(x: Int , y: Int){
            val help : ArrayList<Cell> = ArrayList(81)

            cells.forEach { help.addAll(it) }

            areaCells.forEach { if (help.contains(it))  help.remove(it) }

            help.forEach { if ((it.cell.text == cells[x][y].cell.text) and (it.cell.text != "")) sameValueText.add(it)}
            sameValueText.forEach { it.cell.setBackground(StyleChanger.companion.bordercellsamevalueotherfield) }
        }

        private fun clearSameValueTextBackgroundInOtherField(){
            sameValueText.forEach { it.cell.setBackground(StyleChanger.companion.bordercell) }
            sameValueText.clear()
        }

        private fun clearCellForCheckBackground() {
            cellforCheck.forEach { it.cell.setBackground(StyleChanger.companion.bordercell) }
            cellforCheck.clear()
        }

        private fun clearSameValueBackground() {
            sameValueBackground.forEach { it.cell.setBackground(StyleChanger.companion.bordercell) }
            sameValueBackground.clear()
        }
        private fun clearsameColorText(){
            sameColorText.forEach { it.cell.setTextColor(Color.DKGRAY) }
            sameColorText.clear()
        }

        fun checksameTextColor() {
            val iterator = sameColorText.listIterator()
            while (iterator.hasNext()) {
                val oldvalue = iterator.next()
                areaCells.clear()

                addAreaCells(oldvalue.xLocation , oldvalue.yLocation)

                var help: Int = 0
                for (k in 0..areaCells.size - 2) {
                    if (oldvalue.flagPen == true)
                        ++help
                    else if(((oldvalue.cell.text == "") || (areaCells[k].cell.text ==""))
                            || (oldvalue.cell.text.length > 1) || (areaCells[k].cell.text.length > 1 ))
                        ++help
                    else if ((oldvalue.cell.text.toString().toInt() != areaCells[k].cell.text.toString().toInt()))
                        ++help

                }
                if (help == areaCells.size - 1)
                    if (oldvalue.cell.currentTextColor == Color.RED) {
                        oldvalue.cell.setTextColor(Color.DKGRAY)
                        iterator.remove()
                    }
            }
        }

    }
}





