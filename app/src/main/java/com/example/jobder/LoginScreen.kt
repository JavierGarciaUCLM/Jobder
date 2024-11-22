package com.example.jobder

import android.app.Activity
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
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ui.theme.JobderTheme
import ui.utils.getTranslation
import java.util.concurrent.Executors

/***************************** LoginScreen *****************************/
class LoginScreen:ComponentActivity() {
    //private var isNavigating = false
    //private lateinit var language: String
    private lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.toggleIsNavigating()
        var (isDarkMode, language) = PreferencesHelper.loadPreferences(this)
        println("Iniciando LoginScreen.kt")
        //language = intent.getStringExtra("selectedLanguage") ?: ""
        //appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        setContent {
JobderTheme(colorScheme = SharedState.theme.value) {
            val context = LocalContext.current
            //language = intent.getStringExtra("language") ?: ""
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }
            //var isDarkMode by remember{ mutableStateOf(false) }

            //isDarkMode = intent.getBooleanExtra("isDarkMode",false)
            // Recuperar el valor del Intent
//            LaunchedEffect(Unit) {
//                val intent = (context as? Activity)?.intent
//                isDarkMode = intent?.getBooleanExtra("isDarkMode", false) ?: false
//            }
            //val isDarkMode = appViewModel.isDarkMode // Obtiene el estado de modo oscuro
            //val language by  appViewModel.selectedLanguage
            //appViewModel.toggleIsNavitaing()
            // Fondo basado en el modo oscuro
            val backgroundModifier = if (SharedState.darkModeIsChecked.value) {
                Modifier.fillMaxSize().background(Color.Gray) .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        SharedState.scale.value = (SharedState.scale.value * zoom).coerceIn(SharedState.minScale, SharedState.maxScale)
                    }
                }
            } else {
                Modifier.fillMaxSize() .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        SharedState.scale.value = (SharedState.scale.value * zoom).coerceIn(SharedState.minScale, SharedState.maxScale)
                    }
                }
            }
            Box(modifier = backgroundModifier) {

                // Imagen de fondo
                Image(
                    painter = painterResource(id = R.drawable.ohyeah),
                    contentDescription = null,
                    alpha = 0.4f,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Rectángulo superpuesto
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (SharedState.darkModeIsChecked.value) Color.Black.copy(alpha = 0.5f) else Color.White.copy(
                                alpha = 0.5f
                            )
                        )
                )

                // Logo
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
// Botón de cambio de modo oscuro
                IconButton(
                    onClick = {
                        SharedState.darkModeIsChecked.value = !SharedState.darkModeIsChecked.value
                        SharedState.updateTheme()
                        //isDarkMode = !isDarkMode
                        //PreferencesHelper.savePreferences(context,isDarkMode, language = language)
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

                // Botón de Log in
                Button(
                    onClick = {
                        val intent = Intent(context, NewLoginScreen::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp)
                        .width((200 * SharedState.scale.value).dp)
                        .height((50 * SharedState.scale.value).dp)
                        ,
                    border = if (selectedButtonIndex == 0) BorderStroke(
                        2.dp,
                        Color.Blue
                    ) else null,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(SharedState.theme.value.onPrimaryContainer,
                        SharedState.theme.value.primaryContainer,
                        SharedState.theme.value.secondaryContainer,
                        SharedState.theme.value.onSecondary)
                ) {
                    Text(getTranslation("login", SharedState.language.value), fontSize = (16 * SharedState.scale.value).sp)
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
                                    Log.e("OhhYEah", "Sonrisa detectada!!")
                                    appViewModel.toggleIsNavigating()
                                    val intent = Intent(this, NewLoginScreen::class.java).apply {
                                        putExtra("language", language)
                                        putExtra("isDarkMode", isDarkMode)
                                    }
                                    PreferencesHelper.savePreferences(
                                        this,
                                        isDarkMode,
                                        language = language
                                    )
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

    override fun onBackPressed() {
        super.onBackPressed()
        appViewModel.toggleIsNavigating()
        MainActivity()
    }
    }

