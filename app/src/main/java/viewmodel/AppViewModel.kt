package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var isDarkMode = mutableStateOf(false) // Estado para modo oscuro
}