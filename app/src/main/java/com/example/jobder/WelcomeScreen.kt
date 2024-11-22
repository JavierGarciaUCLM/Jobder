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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ui.utils.getTranslation
import viewmodel.AppViewModel
import java.util.concurrent.Executors

class WelcomeScreen: ComponentActivity() {
    //private lateinit var language: String
    private lateinit var appViewModel: com.example.jobder.AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel = ViewModelProvider(this).get(com.example.jobder.AppViewModel::class.java)//language = intent.getStringExtra("selectedLanguage") ?: ""
        appViewModel.toggleIsNavigating()
        setContent {

            //val language = appViewModel.getLanguage()
            val context = LocalContext.current
            //language = intent.getStringExtra("language") ?:""
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }
            //val isDarkMode = appViewModel.isDarkMode
            var isDarkMode by remember { mutableStateOf(false) }
            //isDarkMode = intent.getBooleanExtra("isDarkMode",false)
            LaunchedEffect(Unit) {
                val intent = (context as? Activity)?.intent
                isDarkMode = intent?.getBooleanExtra("isDarkMode", false) ?: false
            }
            val backgroundColor = Color(0xFFE0F7FA) // Color azul claro para el fondo
            val buttonColor = Color(0xFF0277BD)     // Color azul oscuro para el botón
            // Fondo basado en el modo oscuro
            val backgroundModifier = if (isDarkMode) {
                Modifier.fillMaxSize().background(Color.Gray)
            } else {
                Modifier.fillMaxSize().background(backgroundColor)
            }
            Box(
                backgroundModifier
            ) {
                // Imagen de fondo (edificio)
                Image(
                    painter = painterResource(id = R.drawable.ohyeah), // Reemplaza con la imagen correcta
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
                IconButton(
                    onClick = {
                        isDarkMode = !isDarkMode
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
                // Logo de la app en la parte superior
                val logo = if (isDarkMode) {
                    painterResource(id = R.drawable.img) // Logo en modo oscuro
                } else {
                    painterResource(id = R.drawable.light_mode_icon) // Logo en modo claro
                }
                Image(
                    painter = logo, // Reemplaza con el logo correcto
                    contentDescription = "JobAlba Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp)
                )

                // Caja central para contener los botones y el texto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Texto de bienvenida
                        Text(
                            text = getTranslation("welcome_to_jobder", language),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 20.dp),
                            textAlign = TextAlign.Center
                        )

                        // Botón de "Join as a User"
                        Button(
                            onClick = {
                                intent = Intent(context, SwipeCompanyScreen::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF0277BD)),
                            border = if (selectedButtonIndex == 0) BorderStroke(
                                2.dp,
                                Color.Blue
                            ) else null,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = getTranslation("join_as_a_user", language),
                                color = Color.White
                            )
                        }

                        // Botón de "Join as a Company"
                        Button(
                            onClick = {
                                intent = Intent(context,SwipePersonScreen::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF0277BD)),
                            border = if (selectedButtonIndex == 1) BorderStroke(
                                2.dp,
                                Color.Blue
                            ) else null,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = getTranslation("join_as_a_company", language),
                                color = Color.White
                            )
                        }

                        // Texto de "Forgot password?"
                        Text(
                            text = getTranslation("forgot_password", language),
                            color = Color(0xFF0277BD),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
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
                                    selectedButtonIndex = (selectedButtonIndex + 1) % 2
                                }
                                if (smileDetected && !appViewModel.isNavigating.value) {
                                    appViewModel.toggleIsNavigating()
                                    var intent: Intent? = null
                                    if (selectedButtonIndex == 0){
                                        intent = Intent(this, SwipeCompanyScreen::class.java)
                                    }else if(selectedButtonIndex == 1)
                                        intent = Intent(this,SwipePersonScreen::class.java)
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
