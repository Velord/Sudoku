package com.example.ps_4.sudoku

import android.content.res.AssetManager
import android.media.SoundPool
import android.os.Build
import android.media.AudioManager
import android.media.AudioAttributes
import android.annotation.TargetApi
import android.content.Context
import android.widget.Toast
import android.content.res.AssetFileDescriptor
import java.io.IOException


class SoundManager(context: Context) {
    val contextSound = context
    var mSoundPool : SoundPool? = null
    var mAssetManager : AssetManager? = null
    var mStreamID: Int = 0
    val eraseSound : Int
    val tipSound :Int
    val cancelSound:Int
    val digitSound:Int
    val fragmentSound :Int

    init {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }

        mAssetManager = contextSound.getAssets()

        eraseSound = loadSound("erase.mp3" )
        tipSound = loadSound("tip.mp3")
        cancelSound = loadSound("cancel.mp3")
        digitSound = loadSound("digit.mp3")
        fragmentSound = loadSound("fragment.mp3")
        print("sadas")
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNewSoundPool() {
        val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        mSoundPool = SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build()
    }

    private fun createOldSoundPool() {
        mSoundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0)
    }

    fun playSound(sound: Int): Int {
        if (sound > 0) {
            mStreamID = mSoundPool!!.play(sound, 1F, 1F, 1, 0, 1F)
        }
        return mStreamID
    }

    private fun loadSound(fileName: String): Int {
        val afd: AssetFileDescriptor
        try {
            afd = mAssetManager!!.openFd(fileName)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(contextSound.applicationContext, "Не могу загрузить файл $fileName",
                    Toast.LENGTH_SHORT).show()
            return -1
        }

        return mSoundPool!!.load(afd, 1)
    }

    fun releasesound(){
        mSoundPool!!.release()
        mSoundPool = null;
    }
}