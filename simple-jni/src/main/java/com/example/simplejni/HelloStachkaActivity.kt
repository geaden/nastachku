package com.example.simplejni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class HelloStachkaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloStachkaScreen()
        }
    }
}
