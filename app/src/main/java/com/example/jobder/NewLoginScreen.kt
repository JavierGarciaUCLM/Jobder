package com.example.jobder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
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
import java.util.Locale
import java.util.concurrent.Executors

class NewLoginScreen:ComponentActivity() {
    //private var isNavigating = false
    //private lateinit var language: String
    private lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState:Bundle ?){
        super.onCreate(savedInstanceState)
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.toggleIsNavigating()
        //isNavigating = false
        // Inicializa el ViewModel usando el contexto de la aplicación
        //appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        //language = intent.getStringExtra("language") ?:""
        //appViewModel = ViewModelProvider(this).get(AppViewModel:: class.java)
        setContent {
            //appViewModel.toggleIsNavitaing()
            val context = LocalContext.current
            //val isDarkMode = appViewModel.isDarkMode
            //val language by appViewModel.selectedLanguage
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }
            //val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
            // Variables para almacenar el correo electrónico y la contraseña
            var email by remember {mutableStateOf("")}
            //var password by remember {mutableStateOf("")}
            ///////////////////////////////////////////////////////////////////////
            // SPEECH TO TEXT
            //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            //val context = LocalContext.current
            var username by remember { mutableStateOf(TextFieldValue("")) }
            var password by remember { mutableStateOf(TextFieldValue("")) }
            var speechRecognizer: SpeechRecognizer? = remember { SpeechRecognizer.createSpeechRecognizer(context) }
            var isListening by remember { mutableStateOf(false) }

            // Configuración de SpeechRecognizer
            DisposableEffect(Unit) {
                val listener = object : android.speech.RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}
                    override fun onError(error: Int) {
                        isListening = false
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResults(results: Bundle?) {
                        isListening = false
                        val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                        if (!spokenText.isNullOrEmpty()) {
                            if(SharedState.username.value.text.isEmpty())
                                SharedState.username.value = TextFieldValue(spokenText)
                            else
                                SharedState.password.value = TextFieldValue(spokenText)
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                }

                speechRecognizer?.setRecognitionListener(listener)
                onDispose {
                    speechRecognizer?.destroy()
                }
            }

            // Solicitar permisos
            val requestPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (!isGranted) {
                    Toast.makeText(context, "Se necesita permiso para usar el micrófono", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(Unit) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            ///////////////////////////////////////////////////////////////////////
            // SPEECH TO TEXT
            //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            //var isDarkMode by remember { mutableStateOf(false) }
            //isDarkMode = intent.getBooleanExtra("isDarkMode",false)
//            LaunchedEffect(Unit) {
//                val intent = (context as? Activity)?.intent
//                isDarkMode = intent?.getBooleanExtra("isDarkMode", false) ?: false
//            }
            // Definir colores
            val backgroundColor = Color(0xFFE0F7FA) // Color azul claro para el fondo
            val buttonColor = Color(0xFF0277BD)     // Color azul oscuro para el botón
// Fondo basado en el modo oscuro
            val backgroundModifier = if (SharedState.darkModeIsChecked.value) {
                Modifier.fillMaxSize().background(Color.Gray).pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        SharedState.scale.value = (SharedState.scale.value * zoom).coerceIn(SharedState.minScale, SharedState.maxScale)
                    }
                }
            } else {
                Modifier.fillMaxSize().background(backgroundColor).pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        SharedState.scale.value = (SharedState.scale.value * zoom).coerceIn(SharedState.minScale, SharedState.maxScale)
                    }
                }
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
                            if (SharedState.darkModeIsChecked.value) Color.Black.copy(alpha = 0.5f)
                            else Color.White.copy(
                                alpha = 0.5f
                            )
                        )
                )
                IconButton(
                    onClick = {
                        SharedState.darkModeIsChecked.value = !SharedState.darkModeIsChecked.value
                        SharedState.updateTheme()
                    }, // Cambia el modo oscuro
                    modifier = Modifier
                        //.align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    val icon = if (SharedState.darkModeIsChecked.value) {
                        painterResource(id = R.drawable.baseline_light_mode_24) // Icono de sol
                    } else {
                        painterResource(id = R.drawable.baseline_dark_mode_24) // Icono de luna
                    }
                    Image(painter = icon, contentDescription = null)
                }
                // Logo de la app en la parte superior
                val logo = if (SharedState.darkModeIsChecked.value) {
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
                        text = getTranslation("welcome_to_jobder", SharedState.language.value),
                        //fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 20.dp),
                        fontSize = (16 * SharedState.scale.value).sp
                    )

                    // Campo de texto para el email
                    BasicTextField(
                        value = SharedState.username.value,
                        onValueChange = {SharedState.username.value = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(16.dp),
                        decorationBox = {innerTextField ->
                            if (SharedState.username.value.text.isEmpty()) {
                                Text(text = getTranslation("email", SharedState.language.value), color = Color.Gray, fontSize = (16 * SharedState.scale.value).sp)
                            }
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Campo de texto para la contraseña
                    BasicTextField(
                        value = SharedState.password.value,
                        onValueChange = {SharedState.password.value = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = MaterialTheme.shapes.small)
                            .padding(16.dp),
                        //visualTransformation = PasswordVisualTransformation(),
                        decorationBox = {innerTextField ->
                            if (SharedState.password.value.text.isEmpty()) {
                                Text(
                                    text = getTranslation("password", SharedState.language.value),
                                    color = Color.Gray,
                                    fontSize = (16 * SharedState.scale.value).sp)
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

                    //}
                    // Botón de Iniciar Sesión
                    Button(
                        onClick = {
                            val intent = Intent(context, WelcomeScreen::class.java)
                            startActivity(intent)
                                  },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                            .width((200 * SharedState.scale.value).dp)
                            .height((50 * SharedState.scale.value).dp)
                        ,
                        colors = ButtonColors(
                            SharedState.theme.value.primary,
                            SharedState.theme.value.onPrimary,
                            SharedState.theme.value.secondaryContainer,
                            SharedState.theme.value.onSecondary
                        ),
                                border = if (selectedButtonIndex == 0) BorderStroke(
                            2.dp,
                            Color.Blue
                        ) else null,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = getTranslation("login", SharedState.language.value), fontSize = (16 * SharedState.scale.value).sp)
                    }

                    // Enlace de "Forgot Password"
                    Text(
                        text = getTranslation("forgot_password", SharedState.language.value),
                        color = Color.White,
                        modifier = Modifier.padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    ////////////////////////////////////////////////////////////
                    // Botón para lo de la voz
                    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                    androidx.compose.material.IconButton(
                        onClick = {
                            if (!isListening) {
                                val intent =
                                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                        )
                                        putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE,
                                            Locale.getDefault()
                                        )
                                    }
                                speechRecognizer?.startListening(intent)
                                isListening = true
                            } else {
                                speechRecognizer?.stopListening()
                                isListening = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter =
                            if (isListening)
                                painterResource(id = R.drawable.baseline_mic_24)
                            else
                                painterResource(id = R.drawable.baseline_mic_off_24),
                            contentDescription = null
                        )
                    }
                    ////////////////////////////////////////////////////////////
                    // Botón para lo de la voz
                    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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
                        Text(text = getTranslation("dont_have_an_account", SharedState.language.value), color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = { /* Navegar a la pantalla de registro */}) {
                            Text(text = getTranslation("sign_up", SharedState.language.value), color = buttonColor)
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
                                    appViewModel.toggleIsNavigating()
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

    override fun onResume() {
        super.onResume()
        appViewModel.toggleIsNavigating()
        appViewModel.toggleDarkMode()
    }
}