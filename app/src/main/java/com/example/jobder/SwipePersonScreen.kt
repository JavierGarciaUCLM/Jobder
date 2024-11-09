package com.example.jobder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ui.screens.SwipeableCardsScreen
import ui.screens.SwipeableCardsScreenPerson

class SwipePersonScreen:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //language = intent.getStringExtra("selectedLanguage") ?:""
        //appViewModel = ViewModelProvider(this).get(AppViewModel:: class.java)
        setContent {
            SwipeableCardsScreenPerson()
        }
    }
}