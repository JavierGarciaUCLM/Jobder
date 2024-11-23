package com.example.jobder

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class AppViewModel(application: Application) : AndroidViewModel(application) {
    var isDarkMode = false
    private var selectedLanguage = mutableStateOf(0) // Valor inicial vacío
    var isNavigating = mutableStateOf(false)

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    fun toggleIsNavigating() {
        isNavigating.value = !isNavigating.value
    }

    fun setLanguage(language: Int) {
        selectedLanguage.value = language
        println("Ahora el idioma es ${selectedLanguage.value}")
    }

    fun getLanguage(): String {
        return when(selectedLanguage.value){
            0 -> "English"
            1 -> "Français"
            2 -> "Español"
            else -> "Not Found"
        } // Accede al valor directamente
    }
}