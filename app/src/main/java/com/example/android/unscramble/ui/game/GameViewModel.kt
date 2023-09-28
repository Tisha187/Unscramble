package com.example.android.unscramble.ui.game

import android.content.Context
import android.provider.Settings.Global.getString
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.unscramble.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import kotlin.properties.ReadOnlyProperty

class GameViewModel:ViewModel() {


    private val _score = MutableLiveData<Int>(0)
        val score:LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount:LiveData<Int>
        get() = _currentWordCount

    private var _currentScrambledWord = MutableLiveData<String>()

    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    //list of words used in the game
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("Gamefragment","GameViewModel created!")
        getnextword()
    }

    fun getnextword(){
         currentWord= allWordsList.random()
        val tempword=currentWord.toCharArray()
        tempword.shuffle()
        while(String(tempword).equals(currentWord,true)){
            tempword.shuffle()
        }
        if (wordsList.contains(currentWord)){
            getnextword()
        }
        else{
            _currentScrambledWord.value= String(tempword)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)

        }

    }
    private fun increasescore(){
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }


    fun reinitializeData() {
        _score.value= 0
        _currentWordCount.value = 0
        wordsList.clear()
        getnextword()
    }

    fun isUserWordCorrect(playerWord:String):Boolean{
        if (playerWord.equals(currentWord, true)) {
            increasescore()
            return true
        }
        return false

    }


    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getnextword()
            true
        } else false
    }



}