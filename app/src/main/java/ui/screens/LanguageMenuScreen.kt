package ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jobder.R
import kotlinx.coroutines.delay
import ui.utils.BaseActivity
import viewmodel.AppViewModel
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import java.util.Locale

class myClasLanguageMenuScreen : BaseActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private var currentIndex by mutableStateOf(0)
    private val languages = listOf("English", "Français", "Español")

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        tts = TextToSpeech(this, this)
//        setContent {
//            JobderTheme {
//                val ttsState = remember { mutableStateOf(tts) }
//                LanguageMenuScreen(
//                    appViewModel = AppViewModel(),
//                    onLanguageSelected = { language -> setTTSLanguage(language) },
//                    navController = NavHostController(this),
//                    selectedLanguage = languages[currentIndex],
//                    tts = ttsState.value
//                )
//            }
//        }
//    }

    private fun setTTSLanguage(language: String) {
        val locale = when (language) {
            "English" -> Locale.ENGLISH
            "Français" -> Locale.FRENCH
            "Español" -> Locale("es", "ES")
            else -> Locale.ENGLISH
        }
        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "Idioma no soportado")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            setTTSLanguage(languages[currentIndex])
        } else {
            Log.e("TTS", "Inicialización fallida")
        }
    }

    override fun onDestroy() {
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    @Composable
    fun LanguageMenuScreen(
        appViewModel: AppViewModel,
        onLanguageSelected: (String) -> Unit,
        navController: NavHostController,
        selectedLanguage: String?,
        tts: TextToSpeech
    ) {
        val isDarkMode = appViewModel.isDarkMode.value
        val backgroundColor = Color(0xFFE0F7FA)
        val buttonColor = Color(0xFF0277BD)
        val backgroundModifier = if (isDarkMode) {
            Modifier.fillMaxSize().background(Color.Gray)
        } else {
            Modifier.fillMaxSize().background(backgroundColor)
        }

        var offsetX by remember { mutableStateOf(0f) }
        var swipeEnabled by remember { mutableStateOf(true) }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ohyeah),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            val logo = if (isDarkMode) {
                painterResource(id = R.drawable.img)
            } else {
                painterResource(id = R.drawable.light_mode_icon)
            }
            Image(
                painter = logo,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { performButtonClick(tts) }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            if (swipeEnabled) {
                                if (pan.x > 0) {
                                    offsetX = 1f
                                    navigateToNextButton()
                                    swipeEnabled = false
                                    Log.d("Swipe", "Swipe a la derecha detectado")
                                } else {
                                    offsetX = -1f
                                    navigateToPreviousButton()
                                    swipeEnabled = false
                                    Log.d("Swipe", "Swipe a la izquierda detectado")
                                }
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                languages.forEachIndexed { index, language ->
                    Button(
                        onClick = {
                            onLanguageSelected(language)
                            navController.navigate("login_screen")
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .then(
                                if (index == currentIndex) {
                                    Modifier.border(2.dp, Color.Blue)
                                } else {
                                    Modifier
                                }
                            )
                            .semantics { contentDescription = "Select $language language" }
                    ) {
                        Text(language)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isDarkMode) Color.Black.copy(alpha = 0.5f) else Color.White.copy(
                            alpha = 0.5f
                        )
                    )
            )

            IconButton(
                onClick = { appViewModel.isDarkMode.value = !isDarkMode },
                modifier = Modifier
                    .padding(16.dp)
                    .semantics { contentDescription = if (isDarkMode) "Switch to light mode" else "Switch to dark mode" }
            ) {
                val icon = if (isDarkMode) {
                    painterResource(id = R.drawable.ic_sun)
                } else {
                    painterResource(id = R.drawable.ic_moon)
                }
                Image(painter = icon, contentDescription = null)
            }
        }

        val animatedOffsetX by animateFloatAsState(
            targetValue = offsetX,
            animationSpec = tween(durationMillis = 500)
        )

        LaunchedEffect(swipeEnabled) {
            if (!swipeEnabled) {
                delay(1000L)
                swipeEnabled = true
                Log.d("Swipe", "Swipe habilitado nuevamente")
            }
        }
    }

    private fun navigateToPreviousButton() {
        if (currentIndex > 0) {
            currentIndex--
            setTTSLanguage(languages[currentIndex])
        }
    }

    private fun navigateToNextButton() {
        if (currentIndex < languages.size - 1) {
            currentIndex++
            setTTSLanguage(languages[currentIndex])
        }
    }

    private fun performButtonClick(tts: TextToSpeech) {
        setTTSLanguage(languages[currentIndex])
        tts.speak("Idioma seleccionado: ${languages[currentIndex]}", TextToSpeech.QUEUE_FLUSH, null, null)
    }
}