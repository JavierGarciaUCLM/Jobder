package com.example.jobder

//import android.speech.tts.TextToSpeech
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import com.example.pinchandreversepinch.ui.theme.PinchAndReversePinchTheme
//import com.example.pinchandreversepinch.ui.theme.Theme
//import androidx.compose.material
//import androidx.compose.material.icons.Icons

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.ProvidableCompositionLocal
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ui.theme.JobderTheme
import ui.theme.Theme
import java.util.Locale
import java.util.concurrent.Executors

object SharedState{
    var darkModeIsChecked  = mutableStateOf(false)
    var protanopiaIsChecked= mutableStateOf(false)
    var deuteranopiaIsChecked= mutableStateOf(false)
    var tritanopiaIsChecked= mutableStateOf(false)
    var theme =  mutableStateOf( Theme.Light)
    var scale = mutableStateOf(1f)
    val minScale = 0.8f
    val maxScale = 2f
    var language = mutableStateOf("Español")
    var username = mutableStateOf(TextFieldValue(""))
    var password = mutableStateOf(TextFieldValue(""))
//TalkBack
    var currentIndex = mutableStateOf(0)
var isSwipeProcessed = mutableStateOf(false)
    lateinit var tts: TextToSpeech
    fun updateTheme() {
        SharedState.theme.value = when {
            // Protanopía
            protanopiaIsChecked.value && darkModeIsChecked.value -> Theme.DarkProtanopia
            protanopiaIsChecked.value && !darkModeIsChecked.value -> Theme.lightProtanopia

            // Deuteranopía
            deuteranopiaIsChecked.value && darkModeIsChecked.value -> Theme.DarkDeuteranopia
            deuteranopiaIsChecked.value && !darkModeIsChecked.value -> Theme.lightDeuteranopia

            // Tritanopía
            tritanopiaIsChecked.value && darkModeIsChecked.value -> Theme.DarkTritanopia
            tritanopiaIsChecked.value && !darkModeIsChecked.value -> Theme.lightTritanopia

            // Modo normal
            darkModeIsChecked.value -> Theme.Dark
            else -> Theme.Light
        }
    }
}
class MainActivity : ComponentActivity() {

    private lateinit var appViewModel: AppViewModel
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

                val backgroundColor = Color(0xFFE0F7FA) // Color azul claro para el fondo


                val backgroundModifier = if (SharedState.darkModeIsChecked.value) {
                    Modifier.fillMaxSize().background(Color.Gray)
                } else {
                    Modifier.fillMaxSize().background(backgroundColor)
                }

                Box(modifier = backgroundModifier) {
                    Image(
                        painter = painterResource(id = R.drawable.ohyeah),
                        contentDescription = null,
                        alpha = 0.4f,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
//                     Rectángulo superpuesto
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

                    // Logo de la app en la parte superior
                    val logo = if (SharedState.darkModeIsChecked.value) {
                        painterResource(id = R.mipmap.ic_launcher_foreground) // Logo en modo oscuro
                    } else {
                        painterResource(id = R.mipmap.img) // Logo en modo claro
                    }
                    Image(
                        painter = logo,

                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = 50.dp)
                    )




                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, _, zoom, _ ->
                                    SharedState.scale.value = (SharedState.scale.value * zoom).coerceIn(SharedState.minScale, SharedState.maxScale)
                                }
                            }

                            .then(gestureModifier),
                        contentAlignment = Alignment.Center
                    ) {

                        Column {
                            Button(
                                onClick = {

                                    SharedState.language.value = "English"
                                    val intent = Intent(context, LoginScreen::class.java)


                                    startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width((200 * SharedState.scale.value).dp)
                                    .height((50 * SharedState.scale.value).dp)
                                    .semantics { contentDescription = "Select English language" },
                                colors =if (SharedState.currentIndex.value == 0) {
                                    ButtonColors(
                                    SharedState.theme.value.primary,
                                    SharedState.theme.value.onPrimary,
                                    SharedState.theme.value.secondaryContainer,
                                    SharedState.theme.value.onSecondary
                                )}else{ButtonColors(
                                    SharedState.theme.value.secondary,
                                    SharedState.theme.value.onSecondary,
                                    SharedState.theme.value.secondaryContainer,
                                    SharedState.theme.value.onSecondary
                                )},
                                border = if (SharedState.currentIndex.value == 0) BorderStroke(
                                    2.dp,
                                    Color.Blue
                                ) else null,
                                shape = RoundedCornerShape(8.dp)

                            ) {
                                Text("English", fontSize = (16 * SharedState.scale.value).sp)//,color = theme.primaryContainer)
                                //selectedLanguage = "English"
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {

                                    SharedState.language.value ="Français"
                                    val intent = Intent(context, LoginScreen::class.java)


                                    startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width((200 * SharedState.scale.value).dp)
                                    .height((50 * SharedState.scale.value).dp)

                                    .semantics { contentDescription = "Select French language" },
                                colors = if (SharedState.currentIndex.value == 1) {
                                    ButtonColors(
                                        SharedState.theme.value.primary,
                                        SharedState.theme.value.onPrimary,
                                        SharedState.theme.value.secondaryContainer,
                                        SharedState.theme.value.onSecondary
                                    )}else{ButtonColors(
                                    SharedState.theme.value.secondary,
                                    SharedState.theme.value.onSecondary,
                                    SharedState.theme.value.secondaryContainer,
                                    SharedState.theme.value.onSecondary
                                )},
                                border = if (SharedState.currentIndex.value == 1) BorderStroke(
                                    2.dp,
                                    Color.Blue
                                ) else null,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Français", fontSize = (16 * SharedState.scale.value).sp)//, color = theme.primaryContainer)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {

                                    SharedState.language.value = "Español"
                                    val intent = Intent(context, LoginScreen::class.java)


                                    startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width((200 * SharedState.scale.value).dp)
                                    .height((50 * SharedState.scale.value).dp)
                                    .semantics { contentDescription = "Select Spanish language" },
                                colors = if (SharedState.currentIndex.value == 2) {
                                    ButtonColors(
                                        SharedState.theme.value.primary,
                                        SharedState.theme.value.onPrimary,
                                        SharedState.theme.value.secondaryContainer,
                                        SharedState.theme.value.onSecondary
                                    )}else{ButtonColors(
                                    SharedState.theme.value.secondary,
                                    SharedState.theme.value.onSecondary,
                                    SharedState.theme.value.secondaryContainer,
                                    SharedState.theme.value.onSecondary
                                )},
                                border = if (SharedState.currentIndex.value == 2) BorderStroke(
                                    2.dp,
                                    Color.Blue
                                ) else null,
                                shape = RoundedCornerShape(8.dp),

                                ) {
                                Text("Español", fontSize = (16 * SharedState.scale.value).sp)//, color = theme.primaryContainer)
                            }
                        }
                    }
                    IconButton(
                        onClick = { isDrawerOpen = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint =SharedState.theme.value.background
                        )
                    }
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
                        Image(icon
                            , contentDescription = null)
                    }

                    // Drawer
                    if (isDrawerOpen) {
                        Dialog(onDismissRequest = { isDrawerOpen = false }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { isDrawerOpen = false }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                                        .background(Color.Transparent)
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .weight(0.3f)
                                            .fillMaxSize()
                                            .clickable { isDrawerOpen = false }
                                    )
                                    Box(
                                        modifier = Modifier
                                            .weight(0.7f)
                                            .fillMaxHeight()

                                            .background(SharedState.theme.value.background, RoundedCornerShape(4.dp))
                                            .clickable { /* Prevent click propagation */ }
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = "Dark mode",
                                                color =
                                                SharedState.theme.value.onBackground
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Switch(
                                                checked = SharedState.darkModeIsChecked.value,
                                                onCheckedChange = {
                                                    SharedState.darkModeIsChecked.value = it
                                                    SharedState.updateTheme()
                                                },
                                                colors = SwitchDefaults.colors(

                                                    checkedTrackColor = Color.Gray
                                                    , checkedThumbColor = Color.White

                                                )

                                            )
//                                            Button(
//                                                onClick = { theme = Theme.Light },
//                                                colors = ButtonDefaults.buttonColors(
//                                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                                                )
//                                            ) {
//                                                Text("Modo Claro")
//                                            }
//                                            Spacer(modifier = Modifier.height(16.dp))
//                                            Button(
//                                                onClick = { theme = Theme.Dark },
//                                                colors = ButtonDefaults.buttonColors(
//                                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                                                )
//                                            ) {
//                                                Text("Modo Oscuro")
//                                            }
                                            Text(
                                                text = "Protanopia",
                                                color =SharedState.theme.value.onBackground
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Switch(
                                                checked = SharedState.protanopiaIsChecked.value,
                                                onCheckedChange = {
                                                    SharedState.protanopiaIsChecked.value = it
                                                    SharedState.updateTheme()
                                                },
                                                colors = SwitchDefaults.colors(

                                                    checkedTrackColor = Color.Gray
                                                    , checkedThumbColor = Color.White

                                                )
                                            )
                                            Text(
                                                text = "Deuteranopia",
                                                color =SharedState.theme.value.onBackground
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Switch(
                                                checked = SharedState.deuteranopiaIsChecked.value,
                                                onCheckedChange = {
                                                    SharedState.deuteranopiaIsChecked.value = it
                                                    SharedState.updateTheme()
                                                },
                                                colors = SwitchDefaults.colors(

                                                    checkedTrackColor = Color.Gray
                                                    , checkedThumbColor = Color.White

                                                )
                                            )
                                            Text(
                                                text = "Tritanopia",
                                                color =SharedState.theme.value.onBackground
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Switch(
                                                checked = SharedState.tritanopiaIsChecked.value,
                                                onCheckedChange = {
                                                    SharedState.tritanopiaIsChecked.value = it
                                                    SharedState.updateTheme()
                                                },
                                                colors = SwitchDefaults.colors(

                                                    checkedTrackColor = Color.Gray
                                                    , checkedThumbColor = Color.White

                                                )
                                            )
                                            // Añade más botones aquí
                                        }
                                    }
                                }
                            }
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
                                processImageProxy(
                                    detector,
                                    imageProxy
                                ) { blinkDetected, smileDetected ->
                                    if (blinkDetected) {
                                        SharedState.currentIndex.value = (SharedState.currentIndex.value + 1) % 3
                                    }
                                    if (smileDetected && !appViewModel.isNavigating.value) {
                                        appViewModel.toggleIsNavigating()
                                        if (SharedState.currentIndex.value == 0) {

                                                SharedState.language.value = "English"

                                        } else if (SharedState.currentIndex.value == 1) {

                                                SharedState.language.value = "Français"

                                        } else {

                                                SharedState.language.value = "Español"

                                        }

                                        //println("Selected Language: ${appViewModel.getLanguage()}") // Debería imprimir el idioma seleccionado

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
        //var (isDarkMode,language) = PreferencesHelper.loadPreferences(this)
        appViewModel.toggleIsNavigating()
        //appViewModel.toggleDarkMode()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity()
    }
}












