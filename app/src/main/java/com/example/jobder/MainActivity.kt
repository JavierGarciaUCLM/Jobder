package com.example.jobder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ui.screens.MainScreen
import ui.theme.JobderTheme
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ui.screens.TTSActivity
import java.util.Locale

class MainActivity : ComponentActivity() , OnInitListener{
    private lateinit var tts: TextToSpeech
    private var currentIndex by mutableStateOf(0)
    private val buttonLabels = listOf("Botón 1", "Botón 2", "Botón 3","Hablar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobderTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                //val miClase = TTSActivity()
                //miClase.speak("HOLA")
                // Inicializa TTS
                MainScreen()
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
            }
        }
    }

    override fun onInit(p0: Int) {
        TODO("Not yet implemented")
    }
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
}











