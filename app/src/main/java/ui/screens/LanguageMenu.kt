package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavController
import com.example.jobder.R
import viewmodel.AppViewModel

/***************************** LanguageMenu *****************************/
@Composable
fun LanguageMenu(navController: NavController, onLanguageSelected: (String) -> Unit, appViewModel: AppViewModel) {
    val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
    // Definir colores
    val backgroundColor = Color(0xFFE0F7FA) // Color azul claro para el fondo
    val buttonColor = Color(0xFF0277BD)     // Color azul oscuro para el botón
    // Fondo basado en el modo oscuro
    val backgroundModifier = if (isDarkMode) {
        Modifier.fillMaxSize().background(Color.Gray)
    } else {
        Modifier.fillMaxSize().background(backgroundColor)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ohyeah),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo
        val logo = if (isDarkMode) {
            painterResource(id = R.drawable.img) // Logo en modo oscuro
        } else {
            painterResource(id = R.drawable.light_mode_icon) // Logo en modo claro
        }
        Image(
            painter = logo,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        // Botones de idioma
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                onLanguageSelected("English")
                navController.navigate("login_screen")
            }) {
                Text("English")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                onLanguageSelected("Français")
                navController.navigate("login_screen")
            }) {
                Text("Français")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                onLanguageSelected("Español")
                navController.navigate("login_screen")
            }) {
                Text("Español")
            }
        }
        // Rectángulo superpuesto
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDarkMode) Color.Black.copy(alpha = 0.5f) else Color.White.copy(
                        alpha = 0.5f
                    )
                )
        )
        IconButton(
            onClick = {
                appViewModel.isDarkMode.value = !isDarkMode
            }, // Cambia el modo oscuro
            modifier = Modifier
                //.align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            val icon = if (isDarkMode) {
                painterResource(id = R.drawable.ic_sun) // Icono de sol
            } else {
                painterResource(id = R.drawable.ic_moon) // Icono de luna
            }
            Image(painter = icon, contentDescription = null)
        }
        //}
    }
}