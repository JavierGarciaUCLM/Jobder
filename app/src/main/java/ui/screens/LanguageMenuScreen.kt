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
import androidx.navigation.NavHostController
import com.example.jobder.R
import viewmodel.AppViewModel

@Composable
fun LanguageMenuScreen(
    appViewModel: AppViewModel,
    onLanguageSelected: (String) -> Unit,
    navController: NavHostController,
    selectedLanguage: String?
) {
    val isDarkMode = appViewModel.isDarkMode.value
    val backgroundColor = Color(0xFFE0F7FA)
    val buttonColor = Color(0xFF0277BD)
    val backgroundModifier = if (isDarkMode) {
        Modifier.fillMaxSize().background(Color.Gray)
    } else {
        Modifier.fillMaxSize().background(backgroundColor)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ohyeah),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        val logo = if (isDarkMode) {
            painterResource(id = R.drawable.img)
        } else {
            painterResource(id = R.drawable.light_mode_icon)
        }
        Image(
            painter = logo,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

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
                navController.navigate("login_screen")}) {
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color.Black.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.5f))
        )

        IconButton(
            onClick = { appViewModel.isDarkMode.value = !isDarkMode },
            modifier = Modifier.padding(16.dp)
        ) {
            val icon = if (isDarkMode) {
                painterResource(id = R.drawable.ic_sun)
            } else {
                painterResource(id = R.drawable.ic_moon)
            }
            Image(painter = icon, contentDescription = null)
        }
    }
}