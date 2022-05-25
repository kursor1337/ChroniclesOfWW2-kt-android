package com.kursor.chroniclesofww2.view.menu.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.ActivityGameBinding
import com.kursor.chroniclesofww2.model.Engine

abstract class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    abstract val engineListener: Engine.Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
    }

    protected fun disableScreen() {
        binding.boardView.isEnabled = false
        binding.divisionResourcesMe.isEnabled = false
    }

    protected fun enableScreen() {
        binding.boardView.isEnabled = true
        binding.divisionResourcesMe.isEnabled = true
    }

    fun goToMainScreen() {
        Log.i(TAG, "Going to main screen")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        System.gc()
        startActivity(intent)
    }

    companion object {
        const val TAG = "GameActivity"
    }


}