package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import viewmodel.AppViewModel
import com.example.jobder.R
import ui.utils.getTranslation

/***************************** NewLoginScreen *****************************/
@Composable
fun NewLoginScreen(language: String, navController: NavHostController, appViewModel: AppViewModel) {

    val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
    // Variables para almacenar el correo electrónico y la contraseña
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }



    // Definir colores
    val backgroundColor = Color(0xFFE0F7FA) // Color azul claro para el fondo
    val buttonColor = Color(0xFF0277BD)     // Color azul oscuro para el botón
// Fondo basado en el modo oscuro
    val backgroundModifier = if (isDarkMode) {
        Modifier.fillMaxSize().background(Color.Gray)
    } else {
        Modifier.fillMaxSize().background(backgroundColor)
    }

    Box(modifier = backgroundModifier) {

        // Imagen de fondo de un edificio (debes colocar tu imagen en /res/drawable)
        Image(
            painter = painterResource(id = R.drawable.ohyeah),
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
            painter = logo,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        // Formulario de inicio de sesión
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getTranslation("welcome_to_jobder",language),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Campo de texto para el email
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (email.isEmpty()) {
                        Text(text = getTranslation("email",language), color = Color.Gray)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo de texto para la contraseña
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (password.isEmpty()) {
                        Text(text = getTranslation("password",language), color = Color.Gray)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            // Botón de cambio de modo oscuro
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
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
            // Botón de Iniciar Sesión
            Button(
                onClick = { navController.navigate("welcome_screen")},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(buttonColor)
            ) {
                Text(text = getTranslation("login",language), color = Color.White)
            }

            // Enlace de "Forgot Password"
            Text(
                text = getTranslation("forgot_password",language),
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp),
                textAlign = TextAlign.Center
            )


            // Botón para ir a la pantalla de registro (sign-up)
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
            Row(
                modifier = Modifier
                    //.align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = getTranslation("dont_have_an_account",language), color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { /* Navegar a la pantalla de registro */ }) {
                    Text(text = getTranslation("sign_up",language), color = buttonColor)
                }
            }
            // }
        }
    }
}