package com.example.ps_4.sudoku

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.ps_4.sudoku.R.color.bt_color_on_pause
import kotlinx.android.synthetic.main.activity_game.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.*
import kotlin.collections.*
import kotlin.text.*
import android.content.SharedPreferences
import android.media.SoundPool


class GameActivity : AppCompatActivity() , INewGameFragmentListener , IStyleFragmentListener ,ISetBackground  {

    private val TAG  = "GameActivity"

    private lateinit var current: CurrentCell
    private lateinit var bottom: ButtonHandlerOnBottom
    private lateinit var field: FieldCreator
    private lateinit var sudokufield: Sudoku
    private lateinit var userfield : UserCells
    private lateinit var ruleofwin : Victory
    private lateinit var pausemanager : PauseResume
    private lateinit var cancel: Cancel
    private lateinit var eraser: Eraser
    private lateinit var helper : Helper
    private lateinit var pen: Pen
    private lateinit var clock: Chronometr
    private lateinit var readwritefile : ReadWriter
    private lateinit var fragmentmenu : MenuFragment
    private lateinit var fragmentnewgame : NewGameFragment
    private lateinit var fragmentstylechanger : StyleFragment
    private lateinit var stat : Statistic.companion
    private lateinit var statreadwrite : Statistic.companion.ReadWrite
    private lateinit var soundmanager : SoundManager
    //this property for fragment
    private val manager = supportFragmentManager

    private var countOfBeginCells = (Random().nextInt(80 - 20) + 20)
    private var countOfHelp = 10

    private val filenamesolver  = "sudokuarray"
    private val filenamenotmutable  = "notmutable"
    private val filenamemutable = "mutable"
    private val filenamecurrentcell = "currentcell"
    private val filenametime = "timewhenstopped"

    private val APP_PREFERENCES = "settings"
    private val APP_PREFERENCES_STYLE = "white"
    var mSettings: SharedPreferences? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG , "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        //method of initialization is very important !!! Don't touch !!!
        //init style changer
        styleChangeInit()
        //init current cell
        current = CurrentCell()
        //init buttons 1, 2,3 etc.
        bottom = ButtonHandlerOnBottom()
        //init field
        field = FieldCreator()
        //check win of user
        ruleofwin = Victory()
        //init chronometr
        clock = Chronometr()
        //init read write to file
        readwritefile = ReadWriter()
        //init fragment menu
        fragmentmenu = MenuFragment()
        //init fragment new game
        fragmentnewgame = NewGameFragment()
        //init fragment style change
        fragmentstylechanger = StyleFragment()
        // init stat
        val patheasy = File(filesDir.toString() + "/" + "easystat").toString()
        val pathnormal = File(filesDir.toString() + "/" + "normalstat").toString()
        val pathhard = File(filesDir.toString() + "/" + "hardstat").toString()
        val pathinsane = File(filesDir.toString() + "/" + "insanestat").toString()
        statreadwrite = Statistic.companion.ReadWrite(patheasy , pathnormal , pathhard , pathinsane)
        stat = Statistic.companion
        statreadwrite.read()
        //set text difficulty
        gameDifficulty()
        //init game activity background
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if (!readStyleSettings())
            StyleChanger.companion.whiteStyle(resources)
        setBackground()
    }

    override fun onStart() {
        Log.d(TAG , "onStart")
        super.onStart()
        //init sound
        soundmanager = SoundManager(this.baseContext)
        //init pause resume button
        pausemanager = PauseResume()
        // init pauser resume button
        bt_pause_resume.setOnClickListener {
            pausemanager.pauseIdentification()
            soundmanager.playSound(soundmanager.fragmentSound)
        }
        //init cancel image button
        cancel = Cancel()
        bt_return.setOnClickListener{
            cancel.cancel()
            soundmanager.playSound(soundmanager.cancelSound)
        }
        //init eraser image button
        eraser = Eraser()
        bt_eraser.setOnClickListener {
            eraser.erase(current.current!!.xLocation , current.current!!.yLocation)
            soundmanager.playSound(soundmanager.eraseSound)
        }
        //init hint image button
        helper = Helper()
        bt_help.setOnClickListener {
            helper.hint(current.current!!.xLocation ,current.current!!.yLocation)
            soundmanager.playSound(soundmanager.tipSound)
        }
        //init pen image button
        pen = Pen()
        bt_pen.setOnClickListener {
            pen.penActivated()
            soundmanager.playSound(soundmanager.fragmentSound)
        }
        //listener on newgame button
        bt_newgame.setOnClickListener {
            if (pausemanager.flagPause != true)
                pausemanager.pauseIdentification()
            hideFragment(fragmentmenu)
            hideFragment(fragmentstylechanger)
            showHideFragment(fragmentnewgame, R.id.new_game_holder)
            soundmanager.playSound(soundmanager.fragmentSound)
        }
        // init menu button
        bt_menu.setOnClickListener {
            //later delete this line
            if (pausemanager.flagPause != true)
                pausemanager.pauseIdentification()
            hideFragment(fragmentnewgame)
            hideFragment(fragmentstylechanger)
            showHideFragment(fragmentmenu , R.id.menu_holder)
            soundmanager.playSound(soundmanager.fragmentSound)
        }
        //init style change button
        bt_style_change.setOnClickListener {
            if (pausemanager.flagPause != true)
                pausemanager.pauseIdentification()
            hideFragment(fragmentnewgame)
            hideFragment(fragmentmenu)
            showHideFragment(fragmentstylechanger , R.id.style_holder)
            soundmanager.playSound(soundmanager.fragmentSound)
        }
        //hide fragment after activity is start
        hideFragment(fragmentmenu)
        hideFragment(fragmentnewgame)
        gameStart()
    }

    override fun onResume() {
        Log.d(TAG , "onResume")
        super.onResume()
        //chronometr
        clock.timeResume()
    }

    override fun onPause() {
        Log.d(TAG , "onCreate")
        super.onPause()
        gamePauseandStopActivity()
}

    override fun onStop() {
        Log.d(TAG , "onStop")
        super.onStop()
        gamePauseandStopActivity()
        saveStyleSettings()
        soundmanager.releasesound()
    }


    override fun newGame(value : Int){
        if (ruleofwin.flagvictory != true) {
            stat.curentseriesofvictory = 0
            stat.currentdiff.gamedefeat += 1
        }
        stat.currentdiff.gamesplayed +=1
        //clean all collection of check and current cell
        userfield.check.clean()
        //clean all cells in pen mode
        pen.clear()
        //clean all cells for cancel
        cancel.clear()
        //first of all mix after copy
        sudokufield.generator.mix(Random().nextInt(31) + 7)
        //copy sudoku for delete elem on a field
        userfield.copy(sudokufield.sudoku)
        //set field ""
        sudokufield.clearField()
        //clear central gridView
        grid5.setBackgroundResource(R.color.background_game)
        //set field finally
        countOfBeginCells = (value)
        userfield.setField(countOfBeginCells)
        //
        ruleofwin.count = userfield.notmutable.size
        //set focus on first cell
        current.setCell(field.cells[0][0])
        //chronnometr = 0
        clock.timeWhenStopped = 0
        clock.timeResume()
        //set difficulty
        gameDifficulty()
        //work with pause
        pausemanager.memorializing()
        pausemanager.flagPause = true
        pausemanager.pauseIdentification()

    }

    override fun startTheGameAgain() {
        if (ruleofwin.flagvictory != true) {
            stat.curentseriesofvictory = 0
            stat.currentdiff.gamedefeat += 1
        }
        stat.currentdiff.gamesplayed +=1

        userfield.check.clean()
        pen.clear()
        cancel.clear()
        sudokufield.clearField()

        userfield.setFieldFromNotMutableCells()

        ruleofwin.count = userfield.notmutable.size
        current.setCell(field.cells[0][0])

        //chronnometr = 0
        clock.timeWhenStopped = 0
        clock.timeResume()
        //work with pause
        pausemanager.memorializing()
        pausemanager.flagPause = true
        pausemanager.pauseIdentification()
    }

    override fun hidestylefragment() {
        pausemanager.pauseIdentification()
    }

    override fun refreshView() {
        setBackground()
    }

    override fun setBackground() {
        main_layout.setBackgroundResource(StyleChanger.companion.backgroundgame!!)

        tool_bar.setBackgroundResource(StyleChanger.companion.backgroundtoolbar!!)
        bt_newgame.setBackgroundResource(StyleChanger.companion.backgroundtoolbar!!)
        game_difficulty.setBackgroundResource(StyleChanger.companion.backgroundtoolbar!!)
        chronometr.setBackgroundResource(StyleChanger.companion.backgroundtoolbar!!)
        bt_pause_resume.setBackgroundResource(StyleChanger.companion.backgroundtoolbar!!)

        mainGridLayout.setBackground(StyleChanger.companion.bordergrid!!)
        field.grids.forEach { it.setBackground(StyleChanger.companion.bordergrid!!) }

        actions_for_field.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_eraser.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_help.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_return.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bt_pen.setBackgroundResource(StyleChanger.companion.backgroundgame!!)

        bottom_buttons.setBackgroundResource(StyleChanger.companion.backgroundgame!!)
        bottom.butts.forEach { it.setBackgroundResource(StyleChanger.companion.backgroundgame!!) }
    }


    private fun readStyleSettings() : Boolean{
        if (mSettings!!.contains(APP_PREFERENCES_STYLE)) {
            val a  = mSettings!!.getString(APP_PREFERENCES_STYLE , "")
            if (a == "white")
                StyleChanger.companion.whiteStyle(resources)
            else if (a == "yellow")
                StyleChanger.companion.yellowStyle(resources)
            else if (a == "dark")
                StyleChanger.companion.darkStyle(resources)
            return true
        }
        return false
    }

    private fun saveStyleSettings(){
        val strstyle = StyleChanger.companion.currentstyle
        val editor = mSettings!!.edit()
        editor.putString(APP_PREFERENCES_STYLE, strstyle)
        editor.apply()
    }

    private fun styleChangeInit(){
        StyleChanger.companion.whiteStyle(resources)
    }

    private fun gamePauseandStopActivity(){
        clock.timeStopped()
        if (ruleofwin.flagvictory == true)
            newGame(Random().nextInt( 80 - 20) + 20)
        readwritefile.write()
        statreadwrite.write()
    }

    private fun gameStart(){
        if (!(readwritefile.read())){
            //create new game
            newGame(countOfBeginCells)
            Log.d(TAG , "can not read files")
        }
        else{
            userfield.copy(sudokufield.sudoku)
            grid5.setBackgroundResource(R.color.background_game)
            //set difficulty
            gameDifficulty()
            //work with pause
            pausemanager.memorializing()
            pausemanager.flagPause = true
            pausemanager.pauseIdentification()
        }
        //chronometr
        clock.timeResume()
    }

    private fun showHideFragment(fragment : Fragment , frame  : Int ){
        val transaction = manager.beginTransaction()
        transaction.replace(frame, fragment)
        if (fragment.isAdded)
            transaction.remove(fragment)
        transaction.commit()
    }

    private fun hideFragment(fragment : Fragment){
        val transaction = manager.beginTransaction()
        if (fragment.isAdded)
            transaction.remove(fragment)
        transaction.commit()
    }

    private fun gameDifficulty(){
        when(countOfBeginCells){
            in 20..25 -> {game_difficulty.setText(R.string.difficultyInsane)
            stat.currentdiff = stat.insane}
            in 26..35 -> {game_difficulty.setText(R.string.difficultyHard)
            stat.currentdiff = stat.hard}
            in 36..45 -> {game_difficulty.setText(R.string.difficultyNormal)
            stat.currentdiff  = stat.normal}
            in 46..80 -> {game_difficulty.setText(R.string.difficultyEasy)
            stat.currentdiff = stat.easy}
        }
    }

    private fun statVictory(){
        //work with stat
            ++stat.curentseriesofvictory

            if (stat.curentseriesofvictory  > stat.currentdiff.bestseriesofvictories)
                stat.currentdiff.bestseriesofvictories = stat.curentseriesofvictory

            if (stat.currentdiff.besttime < clock.timeWhenStopped)
                stat.currentdiff.besttime = clock.timeWhenStopped

            stat.currentdiff.averagetime =
                    (((stat.currentdiff.gamewithwin++ * stat.currentdiff.averagetime) + clock.timeWhenStopped) / stat.currentdiff.gamewithwin)
    }


    inner class ReadWriter : IReadWrite{

        val pathsolver = File(filesDir.toString() + "/" + filenamesolver).toString()
        val pathnotmutable = File(filesDir.toString() + "/" + filenamenotmutable).toString()
        val pathmutable = File(filesDir.toString() + "/" + filenamemutable).toString()
        val pathcurrentcell = File(filesDir.toString() + "/" + filenamecurrentcell).toString()
        val pathtimewhenstopped = File(filesDir.toString() + "/" + filenametime).toString()
        var text : StringBuilder = StringBuilder()

        override fun clear(){
            File(pathsolver).writeText(text = "")
            File(pathnotmutable).writeText(text = "")
            File(pathmutable).writeText(text = "")
            File(pathcurrentcell).writeText(text = "")
            File(pathtimewhenstopped).writeText(text = "")
        }

        override fun write(){
            //it is better to be safe
            clear()
            writeSudokuSolver()
            writeNotMutableCells()
            writeMutableCells()
            writeCurrentCell()
            writeTime()
        }

        override fun read() : Boolean{

            if (readSudokuSolver() and readNotMutableCells()
                    and readMutableCells() and readCurrentCell()
                        and readTime())
                return true

            Toast.makeText(this@GameActivity ,  " Read write read" , Toast.LENGTH_SHORT).show()
            return false
        }

        private fun writeTime(){
            text.setLength(0)
            text.append(clock.timeWhenStopped)
            try {
                File(pathtimewhenstopped).writeText(text = text.toString())
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity , e.toString() + "writeTime" , Toast.LENGTH_SHORT).show()
            }
        }

        private fun readTime() : Boolean{
            if (checkFile(pathtimewhenstopped)) {
                try {
                    val time =  StringBuilder(File(pathtimewhenstopped).readText()).toString()
                    clock.timeWhenStopped = time.toString().toLong()
                    return true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity, e.toString() + "readCurrentCell", Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@GameActivity ,  " checkfile readSudokuSolver" , Toast.LENGTH_SHORT).show()
            return false
        }

        private fun writeCurrentCell(){
            text.setLength(0)
            // add x and y coord
            text.append(current.current!!.xLocation)
            text.append(current.current!!.yLocation)
            try {
                File(pathcurrentcell).writeText(text = text.toString())
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity , e.toString() + "writeCurrentCell" , Toast.LENGTH_SHORT).show()
            }
        }

        private fun readCurrentCell() : Boolean{
            if (checkFile(pathcurrentcell)) {
                try {
                    val coord =  StringBuilder(File(pathcurrentcell).readText()).toString()
                    current.setCell(field.cells[coord[0].toString().toInt()][coord[1].toString().toInt()])
                    return true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity , e.toString() + "readCurrentCell" , Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@GameActivity ,  " checkfile readSudokuSolver" , Toast.LENGTH_SHORT).show()
            return false
        }

        private fun writeMutableCells(){
            text.setLength(0)
            val help : ArrayList<Cell> = ArrayList(81)
            field.cells.forEach { it.forEach {  if (!(userfield.notmutable.contains(it))) help.add(it)}}
            help.forEach { text.append((it.xLocation.toString() + '&'  + it.yLocation.toString() + '&' +  '$' +  it.cell.text + '$' +  '#'))}
            try {
                File(pathmutable).writeText(text = text.toString())
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity , e.toString() + "writeMutableCells" , Toast.LENGTH_SHORT).show()
            }
        }

        private fun readMutableCells() : Boolean{
            val array : ArrayList<Pair<Pair<Int, Int> , String>> =  ArrayList(81 - 20)
            if (checkFile(pathmutable)) {
                try {
                    val list  = StringBuilder(File(pathmutable).readText()).toString().split('#')
                    // last element empty remember it
                    for (i in 0..list.size - 2){
                        // format like   [0] - 0(x)&3(y)&
                        // format like   [1] - value
                        val help = list[i].split('$')
                        //parse location
                        val location = help[0].split('&')
                        val value = help[1]
                        array.add(Pair(Pair(location[0].toInt() , location[1].toInt()) , value))
                    }

                    setFieldFromMutableCells(array)
                    return true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity , e.toString() + "readMutableCells" , Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@GameActivity ,  " checkfile readSudokuSolver" , Toast.LENGTH_SHORT).show()
            return false
        }

        private fun readNotMutableCells() : Boolean{
            val array : ArrayList<Pair<Int, Int>> =  ArrayList(20)
            if (checkFile(pathnotmutable)) {
                try {
                    text.setLength(0)
                    text = StringBuilder(File(pathnotmutable).readText())
                    var iter = 0
                    for (i in 0..text.length / 2 - 1 )
                        array.add(Pair(text[iter++].toString().toInt(), text[iter++].toString().toInt()))
                    userfield.notmutable.clear()

                    iter = 0
                    array.forEach { userfield.notmutable.add(field.cells[it.first][it.second])
                                        userfield.notmutable[iter++].cell.text = sudokufield.sudoku[it.first][it.second].toString()}

                    userfield.setFieldFromNotMutableCells()
                    userfield.copyNotMutableCells()

                    ruleofwin.count = userfield.notmutable.size

                    return true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity , e.toString() + "readNotMutableCells" , Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@GameActivity ,  " checkfile readSudokuSolver" , Toast.LENGTH_SHORT).show()
            return false
        }

        private fun writeNotMutableCells(){
            text.setLength(0)
            userfield.notmutable.forEach { text.append( (it.xLocation.toString() + it.yLocation.toString()) ) }
            try {
                File(pathnotmutable).writeText(text = text.toString())
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity , e.toString() + "writeNotMutableCells" , Toast.LENGTH_SHORT).show()
            }
        }

        private fun writeSudokuSolver(){
            text.setLength(0)
            sudokufield.sudoku.forEach { it.forEach {text.append(it.toString()) }}
            try {
                File(pathsolver).writeText(text = text.toString())
            } catch (e: Exception) {
                Toast.makeText(this@GameActivity , e.toString() + "writeSudokuSolver" , Toast.LENGTH_SHORT).show()
            }
        }

        private fun readSudokuSolver() : Boolean {
            val array : ArrayList<Int> = ArrayList(81)
            if (checkFile(pathsolver)) {
                try {
                    StringBuilder(File(pathsolver).readText()).forEach { array.add(it.toString().toInt())}
                    // проверка на изначальное условие
                    var d = 0
                    var gh = 0
                    sudokufield.sudoku.forEach { it.forEach { if (it == array[d++]) gh++ }}
                    if (gh != 81){
                        d = 0
                        for (i in 0..8)
                            for (k in 0..8)
                                sudokufield.sudoku[i][k] = array[d++]
                    }
                    return true
                } catch (e: Exception) {
                    Toast.makeText(this@GameActivity , e.toString() + "readSudokuSolver" , Toast.LENGTH_SHORT).show()
                }
            }
            Toast.makeText(this@GameActivity ,  " checkfile readSudokuSolver" , Toast.LENGTH_SHORT).show()
            return false
        }

        private fun checkFile(path : String): Boolean {
            var result = false
            val fileCheck = File(path)
            if (fileCheck.exists())
                result = true
            return result
        }

        private fun setFieldFromMutableCells( a : ArrayList<Pair<Pair<Int, Int> , String>>){
            a.forEach {
                if (it.second.length > 1){
                    pen.penModeAdd(it.first.first , it.first.second)
                    field.cells[it.first.first][it.first.second].penModeActivated()
                }
                field.cells[it.first.first][it.first.second].cell.text = it.second }
        }
    }

    inner class Chronometr{
        var timeWhenStopped = 0L

        init {
            chronometr.base = SystemClock.elapsedRealtime()
            timeWhenStopped = 0
        }

        fun timeResume(){
            chronometr.base = timeWhenStopped + SystemClock.elapsedRealtime()
            chronometr.start()
        }

        fun timeStopped(){
            timeWhenStopped = chronometr.base - SystemClock.elapsedRealtime()
            chronometr.stop()
        }
    }

    inner class Helper{
        private var countHelp : Int

        init {
            //берем из глоб обл видимости
            countHelp = countOfHelp
        }

        fun setcountHelp(a : Int){
            countHelp +=a
        }

        fun hint(x : Int, y : Int ){
            if ((countHelp > 0) and (pausemanager.flagPause != true)){
                if (!(userfield.notmutable.contains(field.cells[x][y]))) {
                    --countHelp
                    //clear cell and set it
                    userfield.check.removeCellFromCollection(x, y)
                    field.cells[x][y].cell.setTextColor(Color.DKGRAY)
                    field.cells[x][y].cell.text = sudokufield.sudoku[x][y].toString()
                    userfield.notmutable.add(field.cells[x][y])
                    current.setCell(field.cells[x][y])
                }
                Toast.makeText(this@GameActivity ,"clue left $countHelp" ,Toast.LENGTH_SHORT).show()
            }

        }

        fun setCountHelp(a : Int){
            countHelp = a
        }
    }

    inner class Eraser{
        fun erase(x : Int, y: Int){
            if (!(userfield.notmutable.contains(field.cells[x][y]))) {
                if ( field.cells[x][y].flagPen == true) {
                    field.cells[x][y].penModeDeactivated()
                    pen.penModeClear(x, y)
                }
                field.cells[x][y].cell.text = ""
                // work with rule of win count
                current.current!!.ifill = false
                ruleofwin.minus()
                //
                current.check()
            }
        }
    }

    inner class Cancel{
        val changedCells : ArrayList<Pair<Pair<Int, Int>, String>> = ArrayList(5)

        fun add(x :Int, y : Int, valuecurrent : String ,  valuenext : String){
            if (valuecurrent != "" ) {
                if (field.cells[x][y].cell.text.toString() != valuenext)
                    changedCells.add(Pair(Pair(x, y), valuecurrent))
            }
            else
                changedCells.add(Pair(Pair(x, y), valuecurrent))
        }

        fun cancel(){
            if ((changedCells.size > 0) and (pausemanager.flagPause == false)) {
                val a = changedCells.lastIndex
                val x  = changedCells[a].first.first
                val y = changedCells[a].first.second
                if (changedCells[a].second.length > 1) {
                    pen.penModeAdd(x, y)
                }
                else if (field.cells[x][y].flagPen == true) {
                    field.cells[x][y].penModeDeactivated()
                }
                // set text
                field.cells[x][y].cell.text = changedCells[changedCells.lastIndex].second
                //work with flag filling cell
                if ((changedCells[a].second == "") or (changedCells[a].second.length > 1)) {
                    if (field.cells[x][y].ifill == true) {
                        field.cells[x][y].ifill = false
                        ruleofwin.minus()
                    }
                }
                else {
                    if (field.cells[x][y].ifill == false) {
                        field.cells[x][y].ifill = true
                        ruleofwin.plus()
                    }
                }
                //init current cell
                current.setCell(field.cells[x][y])
                // delete
                changedCells.removeAt(a)
            }
        }

        fun clear(){
            changedCells.clear()
        }
    }

    inner class PauseResume{
        // don't touch flag
        var flagPause = true

        private  var fieldOnPause :  ArrayList<String> = ArrayList(81)
        private lateinit  var  cellOnPause :  Pair<Int,Int>

        fun memorializing(){
            fieldOnPause.clear()
            for (i in 0..8)
                for (k in 0..8)
                    fieldOnPause.add(field.cells[i][k].cell.text.toString())
            cellOnPause = Pair(current.current!!.xLocation , current.current!!.yLocation)
        }

        fun pauseIdentification(){
            if (flagPause == true) {

                hideFragment(fragmentmenu)
                hideFragment(fragmentnewgame)
                hideFragment(fragmentstylechanger)

                bt_pause_resume.setImageResource(R.drawable.pause_button)
                var inc = 0
                for (i in 0..8)
                    for (k in 0..8)
                        field.cells[i][k].cell.text = fieldOnPause[inc++]

                current.setCell(field.cells[cellOnPause.first][cellOnPause.second])
                clock.timeResume()
                flagPause = false

                field.setBackgroundGrid()
                bottom.setBackground()


                bt_pause_resume.setImageResource(R.drawable.pause_button)
                bt_newgame.setTextColor(resources.getColor(R.color.bt_color) )
                game_difficulty.setTextColor(resources.getColor(R.color.bt_color))
                chronometr.setTextColor(resources.getColor( R.color.bt_color))
                bt_style_change.setBackgroundResource(R.drawable.paintboard)
                bt_menu.setBackgroundResource(R.drawable.vertical_dots)
                grid5.setBackground(StyleChanger.companion.bordergrid)
            }
            else {
                // keep in mind field and current cell
                memorializing()

                sudokufield.clearField()
                userfield.check.clean()
                clock.timeStopped()
                flagPause = true

                current.current!!.cell.setBackgroundResource(R.drawable.bordercell)
                grid5.setBackgroundResource(R.drawable.play_button_main)
                field.setBackgroundGridOnPause()
                bottom.setBackgroundOnPause()

                bt_pause_resume.setImageResource(R.drawable.play_arrow_on_pause)
                bt_newgame.setTextColor(resources.getColor(R.color.bt_color_on_pause))
                game_difficulty.setTextColor(resources.getColor(R.color.bt_color_on_pause))
                chronometr.setTextColor(resources.getColor(bt_color_on_pause))
                bt_style_change.setBackgroundResource(R.drawable.paintboard_on_pause)
                bt_menu.setBackgroundResource(R.drawable.vertical_dots_on_pause)
                grid5.setBackground(StyleChanger.companion.bordergridonpause)
            }
        }
    }

    inner class Victory {
        var flagvictory = false
        fun add(){
            if (current.current!!.ifill == false){
                ++count
                current.current!!.ifill = true
            }
            if (count >= 81) {
                var inc  = 0
                for (i in 0..8)
                    for (k in 0..8)
                        if (field.cells[i][k].cell.text.toString() == sudokufield.sudoku[i][k].toString())
                            ++inc

                if (inc == 81) {
                    game_difficulty.setText(R.string.userWinner)
                    flagvictory = true
                    clock.timeStopped()
                    statVictory()
                }
            }
        }

        fun plus(){
            ++count
        }

        fun minus(){
            --count
        }

        var count = userfield.notmutable.size
    }

    inner class CurrentCell {
        var current: Cell? = null
        private var flag: Boolean = false
        //change background cell color and check it
        fun setCell(cell: Cell?) {
            if (flag and (cell != current))
                current!!.cell.setBackground(StyleChanger.companion.bordercell)
            flag = true


            current = cell

            check()
        }

        fun check() {
            //проверка ячейки
            userfield.check.checkCell(
                    current!!.xLocation,
                    current!!.yLocation
            )
            checkSameTextColor()
            // set bacground for all frid in view
            field.setBackgroundGrid()
        }
        //проверка всех одинаковых значений после изменения текста ячейки
        private fun checkSameTextColor() {
            userfield.check.checksameTextColor()
            //меняем бекграунд обратно после проверки , т.к. проверка меняет все ячейки
            current!!.cell.setBackgroundResource(StyleChanger.companion.currentcell!!)
        }
    }

    inner class FieldCreator() {
        var cells: Array<Array<Cell>> = Array(9, { Array(9, { Cell(TextView(this@GameActivity), 0, 0) }) })
        val grids: Array<android.support.v7.widget.GridLayout> =
                arrayOf(grid1, grid2, grid3, grid4, grid5, grid6, grid7, grid8, grid9)

        init {
            for (i in 0..8) {
                for (k in 0..8) {
                    grids[i].addView(cells[i][k].cell)
                    cells[i][k].xLocation = i
                    cells[i][k].yLocation = k
                }
            }

            sudokufield = Sudoku(cells)
            userfield = UserCells(cells)

            for (i in 0..8)
                for (k in 0..8) {
                    cells[i][k].cell.setOnClickListener {
                        if (pausemanager.flagPause != true)
                            current.setCell(cells[i][k])
                        else
                            pausemanager.pauseIdentification()

                    }
                }
        }

        fun setBackgroundGrid(){
            grids.forEach { it.setBackground(StyleChanger.companion.bordergrid) }
        }

        fun setBackgroundGridOnPause(){
            grids.forEach { it.setBackground(StyleChanger.companion.bordergridonpause) }
        }
    }

    inner class Pen {
        var onFlag : Boolean
        val penmodeCells : ArrayList<Pair<Int,Int>> = ArrayList(3)
        val default : StringBuilder = StringBuilder("         \n" +
                                                        "         \n" +
                                                        "         ")

        init {
            bt_pen.setImageResource(R.drawable.pen_off)
            onFlag = false
        }

        private fun stringBuilder(str : String , value: String) : StringBuilder{
            val answer : StringBuilder  = StringBuilder()
            str.forEach {answer.append(it)}
            when(value){
                "1" -> {if (str.contains(value))
                    answer[0] = ' '
                else
                    answer[0] = value[0]}
                "2" -> {if (str.contains(value))
                    answer[4] = ' '
                else
                    answer[4] = value[0]}
                "3" -> {if (str.contains(value))
                    answer[8] = ' '
                else
                    answer[8] = value[0]}
                "4" -> {if (str.contains(value))
                    answer[10] = ' '
                else
                    answer[10] = value[0]}
                "5" -> {if (str.contains(value))
                    answer[14] = ' '
                else
                    answer[14] = value[0]}
                "6" -> {if (str.contains(value))
                    answer[18] = ' '
                else
                    answer[18] = value[0]}
                "7" -> {if (str.contains(value))
                    answer[20] = ' '
                else
                    answer[20] = value[0]}
                "8" -> {if (str.contains(value))
                    answer[24] = ' '
                else
                    answer[24] = value[0]}
                "9" -> {if (str.contains(value))
                    answer[28] = ' '
                else
                    answer[28] = value[0]}
            }

            return answer
        }

        fun penMode(value : String){
            //if first enter
            if (current.current!!.flagPen == false) {
                penmodeCells.add(Pair(current.current!!.xLocation, current.current!!.yLocation))
                current.current!!.penModeActivated()
                current.current!!.cell.text = default
            }

            val d : StringBuilder = stringBuilder(current.current!!.cell.text.toString() , value)

            current.current!!.cell.text = d


            current.check()
        }

        fun penModeClear(x : Int , y: Int){
            penmodeCells.remove(Pair(x,y))
            field.cells[x][y].penModeDeactivated()
        }

        fun clear(){
            penmodeCells.forEach { field.cells[it.first][it.second].penModeDeactivated() }
            penmodeCells.clear()
        }

        fun penModeAdd(x :Int , y:Int){
            penmodeCells.add(Pair(x,y))
            field.cells[x][y].penModeActivated()
        }

        fun penActivated(){
            if (onFlag == false){
                bt_pen.setImageResource(R.drawable.pen_on)
                onFlag = true
            }
            else{
                bt_pen.setImageResource(R.drawable.pen_off)
                onFlag = false
            }
        }

    }

    inner class ButtonHandlerOnBottom {
        val butts  = arrayOf(bt_1 ,bt_2 ,bt_3 ,bt_4, bt_5, bt_6, bt_7, bt_8, bt_9)
        init {
            //SAM constructor to reuse event handler
            val listener = View.OnClickListener { view ->
                val text = when (view.id) {
                    R.id.bt_1 -> "1"
                    R.id.bt_2 -> "2"
                    R.id.bt_3 -> "3"
                    R.id.bt_4 -> "4"
                    R.id.bt_5 -> "5"
                    R.id.bt_6 -> "6"
                    R.id.bt_7 -> "7"
                    R.id.bt_8 -> "8"
                    R.id.bt_9 -> "9"
                    else -> "unknown button"
                }
                if (pausemanager.flagPause != true) {
                    if (!(userfield.notmutable.contains(current.current!!))) {
                        //play sound
                        soundmanager.playSound(soundmanager.digitSound)
                        // coordinates cuurent cell
                        val coord = Pair(first = current.current!!.xLocation, second = current.current!!.yLocation)
                        //if pen mode activated  for cell
                        if (pen.onFlag == true) {
                            //add to cancel button
                            cancel.add(coord.first, coord.second, current.current!!.cell.text.toString() , text)
                            //change text in pen mode
                            pen.penMode(text)
                            return@OnClickListener
                        }
                        if (pen.onFlag == false) {
                            if (pen.penmodeCells.contains(Pair(coord.first, coord.second))) {
                                //add to cancel button
                                cancel.add(coord.first, coord.second, current.current!!.cell.text.toString() , text)
                                //first of all change text and flag after all check
                                current.current!!.cell.text = text
                                pen.penModeClear(coord.first, coord.second)
                                current.check()
                                return@OnClickListener
                            }
                            //add to cancel button
                            cancel.add(coord.first, coord.second, current.current!!.cell.text.toString() , text)
                            //init current cell
                            current.current!!.cell.text = text
                            current.check()
                            field.setBackgroundGrid()
                            //counter number of cells remaining to verify the victory
                            ruleofwin.add()
                        }
                    }
                }
            }
            butts.forEach { it.setOnClickListener(listener) }
        }

        @SuppressLint("ResourceAsColor")
        fun setBackground(){
            butts.forEach { it.setTextColor(resources.getColor(R.color.bt_color_in_bottom))}
        }

        fun setBackgroundOnPause(){
            butts.forEach { it.setTextColor(resources.getColor(R.color.bt_color_in_bottom_on_pause))}
        }
    }
}


