package com.example.jobder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ui.utils.getTranslation
import com.example.jobder.AppViewModel
import java.util.concurrent.Executors

class NewLoginScreen:ComponentActivity() {
    //private var isNavigating = false
    //private lateinit var language: String
    private lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState:Bundle ?){
        super.onCreate(savedInstanceState)
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.toggleIsNavitaing()
        //isNavigating = false
        // Inicializa el ViewModel usando el contexto de la aplicación
        //appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        //language = intent.getStringExtra("selectedLanguage") ?:""
        //appViewModel = ViewModelProvider(this).get(AppViewModel:: class.java)
        setContent {
            //appViewModel.toggleIsNavitaing()
            val context = LocalContext.current
            val isDarkMode by appViewModel.isDarkMode
            //val language by appViewModel.selectedLanguage
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }
            //val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
            // Variables para almacenar el correo electrónico y la contraseña
            var email by remember {mutableStateOf("")}
            var password by remember {mutableStateOf("")}


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
                            if (isDarkMode) Color.Black.copy(alpha = 0.5f)
                            else Color.White.copy(
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
                        text = getTranslation("welcome_to_jobder", appViewModel.getLanguage()),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Campo de texto para el email
                    BasicTextField(
                        value = email,
                        onValueChange = {email = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(16.dp),
                        decorationBox = {innerTextField ->
                            if (email.isEmpty()) {
                                Text(text = getTranslation("email", appViewModel.getLanguage()), color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Campo de texto para la contraseña
                    BasicTextField(
                        value = password,
                        onValueChange = {password = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(16.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        decorationBox = {innerTextField ->
                            if (password.isEmpty()) {
                                Text(text = getTranslation("password", appViewModel.getLanguage()), color = Color.Gray)
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
                        onClick = {
                            //navController.navigate("welcome_screen")
                                  },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(buttonColor),
                                border = if (selectedButtonIndex == 0) BorderStroke(
                            2.dp,
                            Color.Blue
                        ) else null,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = getTranslation("login", appViewModel.getLanguage()), color = Color.White)
                    }

                    // Enlace de "Forgot Password"
                    Text(
                        text = getTranslation("forgot_password", appViewModel.getLanguage()),
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
                        Text(text = getTranslation("dont_have_an_account", appViewModel.getLanguage()), color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = { /* Navegar a la pantalla de registro */}) {
                            Text(text = getTranslation("sign_up", appViewModel.getLanguage()), color = buttonColor)
                        }
                    }
                    // }
                }
            }
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val options = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .build()

                val detector = FaceDetection.getClient(options)

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(executor, { imageProxy ->
                            processImageProxy(
                                detector,
                                imageProxy
                            ) { blinkDetected, smileDetected ->
                                if (blinkDetected) {
                                    selectedButtonIndex = (selectedButtonIndex + 1) % 1
                                }
                                if (smileDetected && !appViewModel.isNavigating.value) {
                                    appViewModel.toggleIsNavitaing()
                                    val intent = Intent(this, WelcomeScreen::class.java)
                                    startActivity(intent)
                                }
                            }
                        })
                    }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e("CameraXApp", "Error al iniciar la cámara", exc)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(
        detector: com.google.mlkit.vision.face.FaceDetector,
        imageProxy: ImageProxy,
        onGestureDetected: (Boolean, Boolean) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(image)
                .addOnSuccessListener { faces ->
                    var blinkDetected = false
                    var smileDetected = false
                    for (face in faces) {
                        face.smilingProbability?.let { smileProb ->
                            if (smileProb > 0.5) {
                                Log.e("FaceDetection", "¡Sonrisa detectada!")
                                smileDetected = true
                            }
                        }
                        face.leftEyeOpenProbability?.let { leftEyeProb ->
                            face.rightEyeOpenProbability?.let { rightEyeProb ->
                                if (leftEyeProb < 0.5 && rightEyeProb < 0.5) {
                                    Log.e("FaceDetection", "¡Parpadeo detectado!")
                                    blinkDetected = true
                                }
                            }
                        }
                    }
                    onGestureDetected(blinkDetected, smileDetected)
                }
                .addOnFailureListener { e ->
                    Log.e("FaceDetection", "Error al detectar la cara", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}