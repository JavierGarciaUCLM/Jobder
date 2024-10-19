package com.example.jobder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.example.jobder.ui.theme.JobderTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                MainScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JobderTheme {
        Greeting("Android")
    }
}
@Composable
fun LoginScreen(language: String,navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
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
@Composable
fun MainScreen() {
    AppNavigation()
}
@Composable
fun AppNavigation() {
    var selectedLanguage by remember { mutableStateOf<String?>(null) }
    // Crea el controlador de navegación
    val navController = rememberNavController()

    // Define el gráfico de navegación
    NavHost(navController = navController, startDestination = "language_menu") {
        composable("language_menu") { // Agregar el menú de idiomas como una pantalla
            LanguageMenu(navController) { language ->
                selectedLanguage = language
                navController.navigate("login_screen") // Navegar a la pantalla de inicio de sesión
            }
        }
        composable("login_screen") {
            if (selectedLanguage != null) {
                LoginScreen(language = selectedLanguage!!, navController)
            } else {
                // Redirigir al menú de idiomas si no se ha seleccionado un idioma
                navController.navigate("language_menu") {
                    popUpTo("language_menu") { inclusive = true } // Limpia la pila de navegación
                }
            }
        }
        composable("new_login_screen") {
            if (selectedLanguage != null) {
                NewLoginScreen(language = selectedLanguage!!, navController)
            } else {
                navController.navigate("language_menu") {
                    popUpTo("language_menu") { inclusive = true } // Limpia la pila de navegación
                }
            }
        }
    }
}
@Composable
fun NewLoginScreen(language: String,navController: NavHostController) {
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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {

        // Imagen de fondo de un edificio (debes colocar tu imagen en /res/drawable)
        Image(
            painter = painterResource(id = R.drawable.ohyeah),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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
        }

        // Botón para ir a la pantalla de registro (sign-up)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = dontHaveAnAccountText, color = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { /* Navegar a la pantalla de registro */ }) {
                Text(text = signUpText, color = buttonColor)
            }
        }
    }
}

