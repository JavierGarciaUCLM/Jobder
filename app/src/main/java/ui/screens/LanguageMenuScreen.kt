package ui.screens

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jobder.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import viewmodel.AppViewModel
import java.util.concurrent.Executors

class myClasLanguageMenuScreen(
    language: String,
    navController: NavHostController,
    appViewModel: AppViewModel
) : ComponentActivity() {
    private val MAX_BLINK_INTERVAL = 1000L // 1 segundo
    private var isNavigating = false // Variable para evitar múltiples navegaciones
    private lateinit var selectedLanguage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val isDarkMode = appViewModel.isDarkMode.value
            val backgroundColor = Color(0xFFE0F7FA)
            val buttonColor = Color(0xFF0277BD)
            val context = LocalContext.current
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val executor = Executors.newSingleThreadExecutor()
            var selectedButtonIndex by remember { mutableStateOf(0) }

            // Fondo según el modo de tema
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.ohyeah),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Aquí definimos los botones de idiomas
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        //.align(Alignment.Center)
                        .padding(16.dp),
                    //horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                onLanguageSelected("English")
                                navController.navigate("login_screen")
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
                        }
                        //Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onLanguageSelected("Français")
                                navController.navigate("login_screen")
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
                        }
                        //Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onLanguageSelected("Español")
                                navController.navigate("login_screen")
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
                                processImageProxy(detector, imageProxy) { blinkDetected, smileDetected ->
                                    if (blinkDetected) {
                                        selectedButtonIndex = (selectedButtonIndex + 1) % 3
                                    }
                                    if (smileDetected && !isNavigating) {
                                        isNavigating = true
                                        val intent = Intent(this@myClasLanguageMenuScreen, LoginScreen::class.java)
                                        startActivity(intent)
                                    }
                                }
                            })
                        }


                    val cameraSelector =  CameraSelector.DEFAULT_FRONT_CAMERA
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            this@myClasLanguageMenuScreen,
                            cameraSelector,
                            imageAnalyzer)
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
                    imageProxy.close()  // Cierra imageProxy al final del procesamiento
                }
        }
    }
    }


