package com.example.jobder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import ui.screens.SwipeableCardsScreen
import ui.screens.swipe.SwipeableCard
import viewmodel.AppViewModel


class SwipeCompanyScreen:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //language = intent.getStringExtra("selectedLanguage") ?:""
        //appViewModel = ViewModelProvider(this).get(AppViewModel:: class.java)
        setContent {
            SwipeableCardsScreen()
        }
    }
}