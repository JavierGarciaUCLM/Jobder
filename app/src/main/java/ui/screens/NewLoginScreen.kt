package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.jobder.AppViewModel

/***************************** NewLoginScreen *****************************/
@Composable
fun NewLoginScreen(language: String, navController: NavHostController, appViewModel: AppViewModel) {

    val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
    // Variables para almacenar el correo electrónico y la contraseña
    var email by remember { mutableStateOf("") }
    val emailText = when (language) {
        "English" -> "email"
        "Français" -> "courriel"
        "Español" -> " correo electrónico"
        else -> "Log in"
    }
    var password by remember { mutableStateOf("") }
    val passwordText = when (language) {
        "English" -> "Password"
        "Français" -> "Mot de passe"
        "Español" -> "Contraseña"
        else -> "Log in"
    }
    val welcomeText = when (language) {
        "English" -> "Welcome to Jobder"
        "Français" -> "Bienvenue à Jobder"
        "Español" -> "Bienvenido a Jobder"
        else -> "Log in"
    }
    val loginText = when (language) {
        "English" -> "Log in"
        "Français" -> "Se connecter"
        "Español" -> "Iniciar Sesión"
        else -> "Log in"
    }
    val forgotPasswordText = when (language) {
        "English" -> "Forgot password?"
        "Français" -> "Mot de passe oublié ?"
        "Español" -> "¿Olvidaste tu contraseña?"
        else -> "Log in"
    }
    val dontHaveAnAccountText = when (language) {
        "English" -> "Don't have an account?"
        "Français" -> "Vous n'avez pas de compte ?"
        "Español" -> "¿No tienes una cuenta?"
        else -> "Log in"
    }
    val signUpText = when (language) {
        "English" -> "Sign up"
        "Français" -> "S'inscrire"
        "Español" -> "Registrarse"
        else -> "Log in"
    }


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
        Image(
            painter = painterResource(id = R.drawable.img),
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
                text = welcomeText,
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
                        Text(text = emailText, color = Color.Gray)
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
                        Text(text = passwordText, color = Color.Gray)
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
                onClick = { /* Acción de inicio de sesión */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(buttonColor)
            ) {
                Text(text = loginText, color = Color.White)
            }

            // Enlace de "Forgot Password"
            Text(
                text = forgotPasswordText,
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
                Text(text = dontHaveAnAccountText, color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { /* Navegar a la pantalla de registro */ }) {
                    Text(text = signUpText, color = buttonColor)
                }
            }
            // }
        }
    }
}