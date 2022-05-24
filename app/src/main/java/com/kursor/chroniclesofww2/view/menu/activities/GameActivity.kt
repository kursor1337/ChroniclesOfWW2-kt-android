package com.kursor.chroniclesofww2.view.menu.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.ActivityGameBinding

abstract class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
    }
}