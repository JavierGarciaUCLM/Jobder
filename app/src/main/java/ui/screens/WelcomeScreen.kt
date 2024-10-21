package ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jobder.R
import ui.utils.getTranslation
import viewmodel.AppViewModel

@Composable
fun WelcomeScreen(language: String,navController: NavHostController, appViewModel: AppViewModel) {
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
    Box(
        backgroundModifier
    ) {
        // Imagen de fondo (edificio)
        Image(
            painter = painterResource(id = R.drawable.ohyeah), // Reemplaza con la imagen correcta
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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

        // Logo de la app en la parte superior
        val logo = if (isDarkMode) {
            painterResource(id = R.drawable.img) // Logo en modo oscuro
        } else {
            painterResource(id = R.drawable.light_mode_icon) // Logo en modo claro
        }
        Image(
            painter = logo, // Reemplaza con el logo correcto
            contentDescription = "Jobder Logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        // Caja central para contener los botones y el texto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Texto de bienvenida
                Text(
                    text = getTranslation("welcome_to_jobder",language),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp),
                    textAlign = TextAlign.Center
                )

                // Botón de "Join as a User"
                Button(
                    onClick = { navController.navigate("swipe_screen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0277BD)),
                    shape = RoundedCornerShape(50.dp) // Esquinas redondeadas
                ) {
                    Text(text = getTranslation("join_as_a_user",language), color = Color.White)
                }

                // Botón de "Join as a Company"
                Button(
                    onClick = { navController.navigate("swipe_screen_person")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0277BD)),
                    shape = RoundedCornerShape(50.dp) // Esquinas redondeadas
                ) {
                    Text(text = getTranslation("join_as_a_company",language), color = Color.White)
                }

                // Texto de "Forgot password?"
                Text(
                    text = getTranslation("forgot_password",language),
                    color = Color(0xFF0277BD),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}