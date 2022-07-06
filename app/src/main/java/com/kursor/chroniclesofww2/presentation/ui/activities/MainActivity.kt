package com.kursor.chroniclesofww2.presentation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.objects.Tools

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Tools.init(this)
    }
}