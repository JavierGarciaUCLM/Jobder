package ui

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import viewmodel.AppViewModel
import ui.screens.NewLoginScreen
//import ui.screens.LanguageMenuScreen
import ui.screens.LoginScreen
import ui.screens.MainScreen
import ui.screens.SwipeableCardsScreen
import ui.screens.SwipeableCardsScreenPerson
import ui.screens.WelcomeScreen
import ui.screens.myClasLanguageMenuScreen
import java.util.Locale

/***************************** AppNavigation *****************************/
@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = viewModel()
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "language_menu_screen") {
        composable("language_menu_screen") {

            //val myClassICreated = myClasLanguageMenuScreen()
            var tts by remember { mutableStateOf<TextToSpeech?>(null) }
            //var currentIndex by remember { mutableStateOf(0) }
            //val languages = listOf("English", "Français", "Español")

            val myClassICreated = myClasLanguageMenuScreen(
                appViewModel = appViewModel,
                onLanguageSelected = { language ->
                    selectedLanguage = language
                    Log.d("AppNavigation", "Language selected: $language")
                },
                navController = navController,
                selectedLanguage = selectedLanguage
            )

        }

        composable("login_screen") {
            val myLoginScreen = LoginScreen()
            myLoginScreen.LoginScreen(language = selectedLanguage ?: "English", navController = navController, appViewModel = appViewModel)
        }

        composable("new_login_screen") {
            NewLoginScreen(language = selectedLanguage ?: "English", navController = navController, appViewModel = appViewModel)
        }

        composable("welcome_screen") {
            WelcomeScreen(language = selectedLanguage ?: "English", navController = navController, appViewModel = appViewModel)
        }

        composable("main") {
            MainScreen()
        }

        composable("swipe_screen") {
            SwipeableCardsScreen()
        }

        composable("swipe_screen_person") {
            SwipeableCardsScreenPerson()
        }
    }
}

fun setTTSLanguage(language: String, tts: TextToSpeech) {

        val locale = when (language) {
            "English" -> Locale.ENGLISH
            "Français" -> Locale.FRENCH
            "Español" -> Locale("es", "ES")
            else -> Locale.ENGLISH
        }
        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "Idioma no soportado")
        }

}
