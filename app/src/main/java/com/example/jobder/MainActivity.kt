package com.example.jobder

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ui.theme.JobderTheme
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select
import ui.screens.TTSActivity
import com.example.jobder.AppViewModel
import java.util.Locale
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {
    //private var isNavigating = false
    private lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Iniciando MainScreen.kt")
        //isNavigating = false
        // Inicializa el ViewModel usando el contexto de la aplicación
        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        appViewModel.toggleIsNavigating()


        //enableEdgeToEdge()
        setContent {
            // Crear instancia de AppViewModel
            //val appViewModel = remember { AppViewModel() }
            val isDarkMode by appViewModel.isDarkMode
            //val language by appViewModel.selectedLanguage
            // Observa el valor de isDarkMode
            //val isDarkMode by appViewModel.isDarkMode
            val context = LocalContext.current
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }
            //var selectedLanguage = listOf("English","Français","Español")//by remember {mutableStateOf("")}
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
            //Box(modifier = Modifier.fillMaxSize()) {


                Column(
                    modifier = Modifier
                        //.fillMaxSize()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            //onLanguageSelected("English")
                            //navController.navigate("login_screen")
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .semantics { contentDescription = "Select English language" },
                        border = if (selectedButtonIndex == 0) BorderStroke(
                            2.dp,
                            Color.Blue
                        ) else null,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF0277BD))
                    ) {
                        Text("English")
                        //selectedLanguage = "English"
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            //onLanguageSelected("Français")
                            //navController.navigate("login_screen")
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .semantics { contentDescription = "Select French language" },
                        border = if (selectedButtonIndex == 1) BorderStroke(
                            2.dp,
                            Color.Blue
                        ) else null,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF0277BD))
                    ) {
                        Text("Français")
                        //selectedLanguage = "Français"
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            //onLanguageSelected("Español")
                            //navController.navigate("login_screen")
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .semantics { contentDescription = "Select Spanish language" },
                        border = if (selectedButtonIndex == 2) BorderStroke(
                            2.dp,
                            Color.Blue
                        ) else null,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF0277BD))
                    ) {
                        Text("Español")
                        //selectedLanguage = "Español"
                    }
                }
            //}
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
                            processImageProxy(detector, imageProxy) { blinkDetected, smileDetected ->
                                if (blinkDetected) {
                                    selectedButtonIndex = (selectedButtonIndex + 1) % 3
                                }
                                if (smileDetected &&!appViewModel.isNavigating.value) {
                                    appViewModel.toggleIsNavigating()
                                    if(selectedButtonIndex==0)
                                        appViewModel.setLanguage(0)
                                    else if (selectedButtonIndex == 1)
                                        appViewModel.setLanguage(1)
                                        else
                                            appViewModel.setLanguage(2)

                                    println("Selected Language: ${appViewModel.getLanguage()}") // Debería imprimir el idioma seleccionado

                                    val intent = Intent(this, LoginScreen::class.java)

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
        if(imageProxy!=null) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
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
                        System.out.println("Cerrando proxy")
                        imageProxy.close()
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appViewModel.toggleIsNavigating()
        appViewModel.toggleDarkMode()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity()
    }
}
          //  JobderTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                //val miClase = TTSActivity()
                //miClase.speak("HOLA")
                // Inicializa TTS
              //  MainScreen()
//                tts = TextToSpeech(this, this)
//                //SpeakButton { speak("Hola, ¿cómo estás?") }
//                MyApp(
//                    buttonLabels = buttonLabels,
//                    currentIndex = currentIndex,
//                    onButtonClick = { index -> speak(buttonLabels[index]) },
//                    onSwipeLeft = { navigateToPreviousButton() },
//                    onSwipeRight = { navigateToNextButton() },
//                    onDoubleTap = { performButtonClick() }
//                )
                //SwipeableCardsScreen()
          //  }



//    private fun navigateToPreviousButton() {
//        if (currentIndex > 0) {
//            currentIndex--
//            speak(buttonLabels[currentIndex])
//        }
//    }
//    private fun navigateToNextButton() {
//        if (currentIndex < buttonLabels.size - 1) {
//            currentIndex++
//            speak(buttonLabels[currentIndex])
//        }
//    }
//    private fun performButtonClick() {
//        speak("Haciendo clic en ${buttonLabels[currentIndex]}")
//    }
//    override fun onInit(status: Int) {
//        if (status == TextToSpeech.SUCCESS) {
//            // Configurar el idioma
//            val result = tts.setLanguage(Locale("es", "ES"))
//            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TTS", "Idioma no soportado")
//            } else {
//                // Hacer que el dispositivo hable
//                //speak("Hola, ¿cómo estás?")
//            }
//        } else {
//            Log.e("TTS", "Inicialización fallida")
//        }
//    }
//
//    private fun speak(text: String) {
//        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
//    }
//
//    override fun onDestroy() {
//        if (this::tts.isInitialized) {
//            tts.stop()
//            tts.shutdown()
//        }
//        super.onDestroy()
//    }
//    /*@Composable
//    fun SpeakButton(onClick: () -> Unit) {
//        Button(onClick = onClick) {
//            Text("Hablar")
//        }
//    }*/
//}
//@Composable
//fun MyApp(
//    buttonLabels: List<String>,
//    currentIndex: Int,
//    onButtonClick: (Int) -> Unit,
//    onSwipeLeft: () -> Unit,
//    onSwipeRight: () -> Unit,
//    onDoubleTap: () -> Unit
//) {
//    val swipeThreshold = 1000f // Ajusta este valor según tus necesidades
//    val animationDuration = 500 // Duración de la animación en milisegundos
//    val swipeTimeout = 1000L // Tiempo en milisegundos antes de deshabilitar el swipe
//
//    var offsetX by remember { mutableStateOf(0f) }
//    var swipeEnabled by remember { mutableStateOf(true) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onDoubleTap = { onDoubleTap() }
//                )
//            }
//            .pointerInput(Unit) {
//                detectTransformGestures { _, pan, _, _ ->
//                    if (swipeEnabled) {
//
//                           if( pan.x > 0 ) {
//                                offsetX = 1f // Valor para swipe a la derecha
//                                onSwipeRight()
//                                swipeEnabled = false
//                               Log.d("Swipe", "Swipe a la derecha detectado")
//                            }else {
//
//                                   offsetX = -1f // Valor para swipe a la izquierda
//                                   onSwipeLeft()
//                                   swipeEnabled = false
//                               Log.d("Swipe", "Swipe a la izquierda detectado")
//                           }
//
//                    }
//                }
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            buttonLabels.forEachIndexed { index, label ->
//                Button(
//                    onClick = { onButtonClick(index) },
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .then(
//                            if (index == currentIndex) {
//                                Modifier.border(2.dp, Color.Blue)
//                            } else {
//                                Modifier
//                            }
//                        )
//                ) {
//                    Text(label)
//                }
//            }
//        }
//    }
//    val animatedOffsetX by animateFloatAsState(
//        targetValue = offsetX,
//        animationSpec = tween(durationMillis = animationDuration)
//    )
//    // Usa animatedOffsetX para mover tus botones
//
//// Restablecer swipeEnabled después de swipeTimeout
//    LaunchedEffect(swipeEnabled) {
//        if (!swipeEnabled) {
//            delay(swipeTimeout)
//            swipeEnabled = true
//            Log.d("Swipe", "Swipe habilitado nuevamente")
//        }
//    }












