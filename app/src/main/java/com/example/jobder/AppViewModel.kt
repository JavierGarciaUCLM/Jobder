package com.example.jobder

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class AppViewModel(application: Application):AndroidViewModel(application) {
    var isDarkMode = mutableStateOf(false)
    // Define el estado de idioma seleccionado como un MutableState
    private val _selectedLanguage = mutableStateOf<String>("") // Valor inicial vacío
    val selectedLanguage: State<String> = _selectedLanguage
    var isNavigating = mutableStateOf(false)
    fun toggleDarkMode() {
        isDarkMode.value = !isDarkMode.value
    }
    fun toggleIsNavitaing(){
        isNavigating.value = !isNavigating.value
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
    }
    fun getLanguage() : String {
        return _selectedLanguage.value
    }
}