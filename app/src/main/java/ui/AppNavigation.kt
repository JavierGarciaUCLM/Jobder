package ui

import androidx.compose.runtime.Composable
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
import ui.screens.LanguageMenu
import ui.screens.LoginScreen
import ui.screens.MainScreen
import ui.screens.SwipeableCardsScreen
import ui.screens.WelcomeScreen
import ui.screens.swipe.SwipeableMainScreen

/***************************** AppNavigation *****************************/
@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = viewModel() // Obtener el ViewModel
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    // Crea el controlador de navegación
    val navController = rememberNavController()

    // Define el gráfico de navegación
    NavHost(navController = navController, startDestination = "language_menu") {
        composable("language_menu") { // Agregar el menú de idiomas como una pantalla
            LanguageMenu(navController, { language ->
                selectedLanguage = language
                navController.navigate("login_screen") // Navegar a la pantalla de inicio de sesión
            },appViewModel)
        }
        composable("login_screen") {
            if (selectedLanguage != null) {
                LoginScreen(language = selectedLanguage!!, navController,appViewModel)
            } else {
                // Redirigir al menú de idiomas si no se ha seleccionado un idioma
                navController.navigate("language_menu") {
                    popUpTo("language_menu") { inclusive = true } // Limpia la pila de navegación
                }
            }
        }
        composable("new_login_screen") {
            if (selectedLanguage != null) {
                NewLoginScreen(language = selectedLanguage!!, navController,appViewModel)
            } else {
                navController.navigate("language_menu") {
                    popUpTo("language_menu") { inclusive = true } // Limpia la pila de navegación
                }
            }
        }
        composable("welcome_screen"){
            if (selectedLanguage != null) {
                WelcomeScreen(language = selectedLanguage!!,navController,appViewModel)
            } else {
                navController.navigate("language_menu") {
                    popUpTo("language_menu") { inclusive = true } // Limpia la pila de navegación
                }
            }
        }
        composable("main"){
            MainScreen()
        }
        composable("swipe"){
            SwipeableMainScreen()
        }
        composable("swipe_screen"){
            SwipeableCardsScreen()
        }
    }
}