package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jobder.R
import viewmodel.AppViewModel

/***************************** LoginScreen *****************************/
@Composable
fun LoginScreen(language: String, navController: NavHostController, appViewModel: AppViewModel) {
    val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
    // Fondo basado en el modo oscuro
    val backgroundModifier = if (isDarkMode) {
        Modifier.fillMaxSize().background(Color.Gray)
    } else {
        Modifier.fillMaxSize()
    }
    Box(modifier = backgroundModifier) {
        val loginText = when (language) {
            "English" -> "Log in"
            "Français" -> "Se connecter"
            "Español" -> "Iniciar Sesión"
            else -> "Log in"
        }
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ohyeah),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )
        // Botón de cambio de modo oscuro
        IconButton(
            onClick = { appViewModel.isDarkMode.value = !isDarkMode }, // Cambia el modo oscuro
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            val icon = if (isDarkMode) {
                painterResource(id = R.drawable.ic_sun) // Icono de sol
            } else {
                painterResource(id = R.drawable.ic_moon) // Icono de luna
            }
            Image(painter = icon, contentDescription = null)
        }

        // Botón de Log in
        Button(
            onClick = {
                // Navegar a la nueva pantalla de inicio de sesión
                navController.navigate("new_login_screen")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Text(loginText)
        }
    }
}