package com.example.jobder

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select
import ui.screens.TTSActivity
import viewmodel.AppViewModel
import java.util.Locale
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            println("Iniciando MainScreen.kt")
// Initialize TextToSpeech
        SharedState.tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                SharedState.tts.language = Locale("es", "ES")
            }
        }

        //var currentIndex by remember { mutableStateOf(0) }
        //val context = LocalContext.current
        val languages = listOf( "English", "Français","Español")
        val buttons = listOf( "Button", "Bouton","Botón")
        val clicking_on = listOf( "Clicking on", "en cliquant sur","Haciendo clic en")
        val selected_button = listOf( "Selected Button", "Bouton sélectionné","Botón seleccionado")
        val languageLocales = listOf( Locale.UK, Locale.FRENCH,Locale("es", "ES"))

        // Cambia el idioma del TTS según el índice actual
        fun updateTTSLanguage(index: Int) {
            val locale = languageLocales[index]
            val result = SharedState.tts.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported: $locale")
            }
        }

            appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
            appViewModel.toggleIsNavigating()
            val (isDarkMode, language) = PreferencesHelper.loadPreferences(this)

            setContent {
            //var theme by remember { mutableStateOf(Theme.Light) }
            var isDrawerOpen by remember { mutableStateOf(false) }
            JobderTheme(colorScheme = SharedState.theme.value) {


                var context = LocalContext.current
                // Recuerda el detector de gestos
                val gestureModifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            val buttonName = buttons.get(SharedState.currentIndex.value)+languages.get(SharedState.currentIndex.value)
                            Log.d("Gesture", "Double Tap on $buttonName")

                            SharedState.tts.speak(clicking_on.get(SharedState.currentIndex.value)+ buttonName, TextToSpeech.QUEUE_FLUSH, null, null)
                            SharedState.language.value = languages.get(SharedState.currentIndex.value)
                            val intent = Intent(context, LoginScreen::class.java)


                            startActivity(intent)
                        }
                    )
                }.pointerInput(Unit) {

                    detectHorizontalDragGestures(
                        onDragStart = {
                            SharedState.isSwipeProcessed.value = false
                        },onHorizontalDrag = { _, dragAmount ->
                            if(!SharedState.isSwipeProcessed.value){
                                if (dragAmount > 0f) {
                                    SharedState.currentIndex.value = (SharedState.currentIndex.value + 1).mod(3)
                                    updateTTSLanguage(SharedState.currentIndex.value)
                                    SharedState.isSwipeProcessed.value = true
                                    SharedState.tts.speak(selected_button.get(SharedState.currentIndex.value)+ languages[SharedState.currentIndex.value], TextToSpeech.QUEUE_FLUSH, null, null)

                                } else {
                                    SharedState.currentIndex.value = (SharedState.currentIndex.value - 1).mod(3)
                                    updateTTSLanguage(SharedState.currentIndex.value)
                                    SharedState.isSwipeProcessed.value = true
                                    SharedState.tts.speak(selected_button.get(SharedState.currentIndex.value)+ languages[SharedState.currentIndex.value], TextToSpeech.QUEUE_FLUSH, null, null)

                                }

                            }},
                        onDragEnd = {
                            SharedState.isSwipeProcessed.value = false
                        }
                    )
                }
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                val executor = Executors.newSingleThreadExecutor()

            val shakeDetector = rememberUpdatedState(ShakeDetector(
                context = LocalContext.current,
                onShakeStart = {
                    showSnowfall = true
                    generateSnowflakes(snowflakes, 50) // Genera 50 copos de nieve al detectar una sacudida
                },
                onShakeStop = { showSnowfall = false }
            ))

            DisposableEffect(Unit) {
                onDispose {
                    shakeDetector.value.unregister()
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(id = R.drawable.ohyeah),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                        shape = RoundedCornerShape(8.dp)
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
                        shape = RoundedCornerShape(8.dp)
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
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Español")
                        //selectedLanguage = "Español"
                    }
                }
                Snowfall(snowflakes)
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
                                if (smileDetected ) {


                                    val intent = Intent(this, LoginScreen::class.java).apply {
                                        putExtra("selectedLanguage",selectedLanguage[selectedButtonIndex])
                                    }

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












