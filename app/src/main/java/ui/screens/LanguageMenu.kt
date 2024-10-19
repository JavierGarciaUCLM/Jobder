package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobder.R

/***************************** LanguageMenu *****************************/
@Composable
fun LanguageMenu(navController: NavController, onLanguageSelected: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
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
    }
}