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
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ui.utils.getTranslation
import viewmodel.AppViewModel
import java.util.concurrent.Executors

/***************************** LoginScreen *****************************/
public class LoginScreen:ComponentActivity() {
    private var isNavigating = false
    private lateinit var language: String
    private lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = intent.getStringExtra("selectedLanguage") ?: ""
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        setContent {
            val context = LocalContext.current
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }
            val isDarkMode = appViewModel.isDarkMode.value // Obtiene el estado de modo oscuro
            // Fondo basado en el modo oscuro
            val backgroundModifier = if (isDarkMode) {
                Modifier.fillMaxSize().background(Color.Gray)
            } else {
                Modifier.fillMaxSize()
            }
            Box(modifier = backgroundModifier) {

                // Imagen de fondo
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
                            if (isDarkMode) Color.Black.copy(alpha = 0.5f) else Color.White.copy(
                                alpha = 0.5f
                            )
                        )
                )
                // Logo
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
                // Botón de cambio de modo oscuro
                IconButton(
                    onClick = {
                        appViewModel.isDarkMode.value = !isDarkMode
                    }, // Cambia el modo oscuro
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    val icon = if (isDarkMode) {
                        painterResource(id = R.drawable.ic_sun) // Icono de sol
                    } else {
                        painterResource(id = R.drawable.ic_moon) // Icono de luna
                    }
                    Image(painter = icon, contentDescription = null)
                }

                // Botón de Log in
                Button(
                    onClick = {
                        // Navegar a la nueva pantalla de inicio de sesión
                        //navController.navigate("new_login_screen")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                            border = if (selectedButtonIndex == 0) BorderStroke(
                        2.dp,
                        Color.Blue
                    ) else null,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(getTranslation("login", language))
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
                                if (smileDetected && !isNavigating) {
                                    Log.e("OhhYEah","Sonrisa detectada!!")
                                    isNavigating = true
                                    val intent = Intent(this, NewLoginScreen::class.java).apply {
                                        putExtra("selectedLanguage",language)
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

