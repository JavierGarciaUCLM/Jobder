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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ui.screens.TTSActivity
import java.util.Locale

class MainActivity : ComponentActivity() , OnInitListener{
    private lateinit var tts: TextToSpeech
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
                //MainScreen()
                tts = TextToSpeech(this, this)
                SpeakButton { speak("Hola, ¿cómo estás?") }
                //SwipeableCardsScreen()
            }
        }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configurar el idioma
            val result = tts.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Idioma no soportado")
            } else {
                // Hacer que el dispositivo hable
                speak("Hola, ¿cómo estás?")
            }
        } else {
            Log.e("TTS", "Inicialización fallida")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
    @Composable
    fun SpeakButton(onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text("Hablar")
        }
    }
}









