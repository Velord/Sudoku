package com.example.ps_4.sudoku

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.Log

class StyleChanger {
        object companion {
                val TAG  = "Style companion"
                var currentstyle : String? = null

                var backgroundgame : Int? = null
                var backgroundtoolbar : Int? = null

                var currentcell : Int? = null
                var butsinbottom : Int? = null
                var butsinbottomonpause : Int? = null

                var bordergrid: Drawable? =   null
                var bordergridonpause: Drawable? =   null
                var bordercell : Drawable? =   null
                var bordercellsamevalue : Drawable? =   null
                var bordercellsamevalueotherfield: Drawable? =   null
                var bordercellforcheck  : Drawable? =   null

                fun whiteStyle(res: Resources){
                        currentstyle = "white"
                        backgroundgame = R.color.background_game
                        backgroundtoolbar = R.color.background_game

                        currentcell = R.color.currentcell
                        butsinbottom = R.color.bt_color_in_bottom
                        butsinbottomonpause = R.color.bt_color_in_bottom_on_pause

                        bordergrid =  ResourcesCompat.getDrawable(res ,R.drawable.border , null)
                        bordergridonpause =  ResourcesCompat.getDrawable(res ,R.drawable.borderonpause , null)
                        bordercell = ResourcesCompat.getDrawable(res ,R.drawable.bordercell , null)

                        bordercellsamevalue = ResourcesCompat.getDrawable(res ,R.drawable.bordercellsamevalue , null)
                        bordercellsamevalueotherfield = ResourcesCompat.getDrawable(res ,R.drawable.bordercellsamevaluotherfield , null)
                        bordercellforcheck = ResourcesCompat.getDrawable(res ,R.drawable.bordercellforcheck , null)
                }

                fun yellowStyle(res : Resources ){
                        currentstyle = "yellow"
                        backgroundgame = R.color.background_game_yellow
                        backgroundtoolbar = R.color.background_game_yellow

                        currentcell = R.color.currentcell
                        butsinbottom = R.color.bt_color_in_bottom
                        butsinbottomonpause = R.color.bt_color_in_bottom_on_pause

                        bordergrid = ResourcesCompat.getDrawable(res, R.drawable.borderyellow, null)
                        bordergridonpause = ResourcesCompat.getDrawable(res, R.drawable.borderonpauseyellow, null)

                        bordercellforcheck = ResourcesCompat.getDrawable(res, R.drawable.bordercellforcheckyellow, null)
                        bordercellsamevalueotherfield = ResourcesCompat.getDrawable(res, R.drawable.bordersamevalueotherfieldyellow, null)
                }

                fun darkStyle(res: Resources ){
                        currentstyle = "dark"
                        backgroundgame = R.color.background_game_dark
                        backgroundtoolbar = R.color.background_toolbar_dark

                        currentcell = R.color.currentcelldark
                        butsinbottom = R.color.bt_color_in_bottom_dark
                        butsinbottomonpause = R.color.bt_color_in_bottom_on_pause_dark

                        bordergrid = ResourcesCompat.getDrawable(res, R.drawable.borderdark, null)
                        bordergridonpause = ResourcesCompat.getDrawable(res, R.drawable.borderonpausedark, null)

                        bordercellsamevalue = ResourcesCompat.getDrawable(res, R.drawable.bordercellsamevaluedark, null)
                        bordercellforcheck = ResourcesCompat.getDrawable(res, R.drawable.bordercellforchekdark, null)
                        bordercellsamevalueotherfield = ResourcesCompat.getDrawable(res, R.drawable.bordercellsamevaluotherfielddark, null)
                }
        }
}