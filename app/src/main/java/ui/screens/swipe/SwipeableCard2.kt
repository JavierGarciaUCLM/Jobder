//package ui.screens.swipe
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.gestures.Orientation
//import androidx.compose.foundation.gestures.draggable
//import androidx.compose.foundation.gestures.rememberDraggableState
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Popup
//import androidx.core.content.ContextCompat.startActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.jobder.Augmented_Reality_Activity
//import com.example.jobder.NewLoginScreen
//import com.example.jobder.R
//import com.example.jobder.SharedState
//import com.google.ar.sceneform.CameraNode
//import com.google.mediapipe.framework.image.BitmapImageBuilder
//import com.google.mediapipe.tasks.core.BaseOptions
//import com.google.mediapipe.tasks.vision.core.RunningMode
//import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
//import io.github.sceneview.Scene
////import io.github.sceneview.Scene
//import io.github.sceneview.SceneView
//import io.github.sceneview.math.Position
//import io.github.sceneview.node.ModelNode
//import io.github.sceneview.node.Node
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
////import io.github.sceneview.ar.Scene
////import io.github.sceneview.math.Position
////import io.github.sceneview.node.ModelNode
////import io.github.sceneview.node.Node
////import io.github.sceneview.utils.TAG
////import kotlinx.coroutines.launch
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.lifecycle.ReportFragment.Companion.reportFragment
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//// Define los posibles estados de la tarjeta
//enum class SwipeDirection {
//    DEFAULT, ACCEPTED, REJECTED
//}
//object SharedStatePopUp {
//    var text_i_want_to_show = mutableStateOf("")
//    var description = mutableStateOf("")
//    var position = mutableStateOf(Position(0f,0f,0f))
//}
//
//// Tarjeta individual deslizante
//@Composable
//fun SwipeableCard(
//    item: Int, // Cambiado a Int para el ID del recurso de la imagen
//    onSwipeLeft: () -> Unit,
//    onSwipeRight: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//     val baseOptionsBuilder = BaseOptions.builder().setModelAssetPath("gesture_recognizer.task")
//     val baseOptions = baseOptionsBuilder.build()
//
//     val optionsBuilder =
//        GestureRecognizer.GestureRecognizerOptions.builder()
//            .setBaseOptions(baseOptions)
//            .setMinHandDetectionConfidence(0.5f)
//            .setMinTrackingConfidence(0.5f)
//            .setMinHandPresenceConfidence(0.5f)
//            .setRunningMode(RunningMode.IMAGE)
//
//     val options = optionsBuilder.build()
//     var gestureRecognizer: GestureRecognizer? = null
//
//     val getContent =
//        registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri: Uri? ->
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO){
//                    uri?.let{ mediaUri ->
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//                            val source = ImageDecoder.createSource(
//                                contentResolver, mediaUri
//                            )
//                            ImageDecoder.decodeBitmap(source)
//                        }else{
//                            MediaStore.Images.Media.getBitmap(
//                                contentResolver, mediaUri
//                            )
//                        }
//                    }?.copy(Bitmap.Config.ARGB_8888,true)?.let{
//                            bitmap ->
//                        val mpImage = BitmapImageBuilder(bitmap).build()
//
//                        val result = gestureRecognizer?.recognize(mpImage)
//                        if(result != null){
//                            withContext(Dispatchers.Main){
//                                if(result.gestures().isNotEmpty()){
//                                    if(result.gestures()[0].isNotEmpty()){
////                                        textView.text = result.gestures()[0][0].categoryName()
////                                        Log.e("hey",result.gestures()[0][0].categoryName())
//                                        when(result.gestures()[0][0].categoryName()){
//                                            "ILoveYou" -> Log.e("YES","ILOVEYOUU")
//                                            "Victory" -> Log.e("YES","VICTORYY")
//                                            "Thumb_Up" -> Log.e("YES","THUMB UPPP")
//                                            "Thumb_Down" -> Log.e("YESSS","THUMB_DOWN")
//                                            "Open_Palm" -> Log.e("YESSS","OPEN PALMMM")
//                                            "Closed_Fist" -> Log.e("YESSS","CLOSED FIISTT")
//                                        }
//                                        if(result.gestures()[0][0].categoryName() == "ILoveYou")
//                                            Log.e("YESS!!!","ILOVEYOUUU")
//                                    }
//                                }
//
//
//
////                                if(result.handednesses().isNotEmpty()){
////                                    if(result.handednesses()[0].isNotEmpty()){
////                                        textView.append(result.handednesses()[0][0].displayName())
////                                    }
////                                }
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//
//
//
//
//
//    var crossHasBeenClicked = false
//    val context = LocalContext.current
//    // Variable para el desplazamiento de la tarjeta
//    var offsetX by remember { mutableStateOf(0f) }
//    val swipeThreshold = 20f // Reducir el umbral para detectar el gesto de deslizamiento
//    //var isRejectedPressed by remember { mutableStateOf(false) }
//    //var isAcceptedPressed by remember { mutableStateOf(false) }
//    // Animar el desplazamiento de la tarjeta para una transición suave
//    val animatedOffsetX by animateFloatAsState(
//        targetValue = offsetX,
//        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
//    )
//
//    // Agregar rotación mientras se desliza la tarjeta
//    val rotationDegree by remember {
//        derivedStateOf {
//            (animatedOffsetX / 30).coerceIn(-20f, 20f) // Limitar la rotación
//        }
//    }
//
//    // Usa remember para optimizar la recomposición de este estado derivado
//    val swipeState by remember {
//        derivedStateOf {
//            when {
//                animatedOffsetX > swipeThreshold -> SwipeDirection.ACCEPTED // Deslizado a la derecha
//                animatedOffsetX < -swipeThreshold -> SwipeDirection.REJECTED // Deslizado a la izquierda
//                else -> SwipeDirection.DEFAULT // En el centro o por defecto
//            }
//        }
//    }
//
//    Box(
//        modifier = modifier
//
//            .fillMaxSize()
//
//            .clip(RoundedCornerShape(10.dp))
//            .graphicsLayer(
//                translationX = animatedOffsetX, // Mueve la tarjeta con animación
//                rotationZ = rotationDegree // Aplica la rotación a la tarjeta
//            )
//            .background(SharedState.theme.value.background, shape = RoundedCornerShape(10.dp))
//            .draggable(
//                orientation = Orientation.Horizontal,
//                state = rememberDraggableState { delta ->
//                    offsetX += delta * 2 // Aumentar la sensibilidad del arrastre
//                },
//                onDragStopped = {
//                    when (swipeState) {
//                        SwipeDirection.ACCEPTED -> {
//                            onSwipeRight()
//                            offsetX = 0f // Reinicia la posición
//                        }
//
//                        SwipeDirection.REJECTED -> {
//                            onSwipeLeft()
//                            offsetX = 0f // Reinicia la posición
//                        }
//
//                        else -> {
//                            // Vuelve al centro más lentamente
//                            offsetX = animatedOffsetX * 0.5f
//                        }
//                    }
//                }
//            )
//            .padding(16.dp)
//    ) {
//        val logo = if (SharedState.darkModeIsChecked.value) {
//            painterResource(id = R.mipmap.ic_launcher_foreground) // Logo en modo oscuro
//        } else {
//            painterResource(id = R.mipmap.img) // Logo en modo claro
//        }
//
//        //Spacer(modifier = Modifier.height(16.dp))
//        // Contenido de la tarjeta
//        Image(
//            painter = painterResource(id = item),
//            contentDescription = null,
//            modifier =
//            Modifier
//                .fillMaxSize()
//                .clip(RoundedCornerShape(50.dp))
//                //.align(Alignment.Center)
//
//
//
//        )
////        Row(
////            modifier = Modifier
////                .align(Alignment.TopCenter)
////        ) {
//            IconButton(
//               onClick = {},
//                modifier = Modifier
//                    .size(40.dp)
//                    .align(Alignment.TopStart)
//                    //.align(Alignment.TopEnd)
//                    .padding(top = 10.dp)
//
//            ){
//                Image(
//                    painter = painterResource(id = R.drawable.topstart),
//                    modifier = Modifier.fillMaxSize(),
//                    contentDescription = null
//                )
//            }
//            IconButton(
//
//                modifier = Modifier
//                    .size(30.dp)
//                    .align(Alignment.TopCenter)
//                    .padding(top = 10.dp),
//                onClick = {}
//            ){
//                Image(
//                    logo,contentDescription = null
//                )
//            }
//                IconButton(
//                    onClick = {},
//                modifier = Modifier
//                    .size(30.dp)
//                    .align(Alignment.TopEnd)
//                    .padding(top = 10.dp)
//            ){ Image(
//                    painter = painterResource(id = R.drawable.chat),
//                    modifier = Modifier.fillMaxSize(),
//                    contentDescription = null
//                )
//            }
//        Row(modifier = Modifier.align(Alignment.BottomCenter)){
//        Box(
//            modifier = Modifier
//                .size(70.dp) // Tamaño del círculo
//                .clip(CircleShape) // Hace la caja circular
//                .background(Color.White)
//                .padding(10.dp)
//                ,//.align(Alignment.BottomStart), // Fondo del círculo
//            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
//        ){
//            IconButton(
//                onClick = {},
//                modifier = Modifier.size(50.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.volver),
//                    contentDescription = null,
//                    modifier = Modifier.size(30.dp)
//                )
//            }
//        }
//        Box(
//            modifier = Modifier
//                .size(70.dp) // Tamaño del círculo
//                .clip(CircleShape) // Hace la caja circular
//                .background(Color.White)
//                .padding(10.dp)
//                ,//.align(Alignment.BottomCenter), // Fondo del círculo
//            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
//        ){
//            IconButton(
//                onClick = {
//                    // Simular el swipe hacia la izquierda
//                    offsetX = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe
//                    onSwipeLeft()
//                    offsetX = 0f // Reinicia la posición
//                },
//                modifier = Modifier.size(50.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.rechazar),
//                    contentDescription = null,
//                    modifier = Modifier.size(30.dp)) }
//        }
//        Box(
//            modifier = Modifier
//                .size(70.dp) // Tamaño del círculo
//                .clip(CircleShape) // Hace la caja circular
//                .background(Color.White)
//                .padding(10.dp)
//                ,//.align(Alignment.BottomCenter), // Fondo del círculo
//            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
//        ){
//            IconButton(
//                onClick = {},
//                modifier = Modifier.size(50.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.estrella),
//                    contentDescription = null,
//                    modifier = Modifier.size(30.dp)
//                ) }
//        }
//        Box(
//            modifier = Modifier
//                .size(70.dp) // Tamaño del círculo
//                .clip(CircleShape) // Hace la caja circular
//                .background(Color.White)
//                .padding(10.dp)
//                ,//.align(Alignment.BottomCenter), // Fondo del círculo
//            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
//        ){
//            IconButton(
//                onClick = {
//                    offsetX = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe
//                    onSwipeRight()
//                    offsetX = 0f // Reinicia la posición
//                },
//                modifier = Modifier.size(50.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.corazoncito),
//                    contentDescription = null,
//                    modifier=Modifier.size(30.dp)
//                )
//            }
//        }
//        Box(
//            modifier = Modifier
//                .size(70.dp) // Tamaño del círculo
//                .clip(CircleShape) // Hace la caja circular
//                .background(Color.White)
//                .padding(10.dp)
//                ,//.align(Alignment.BottomEnd), // Fondo del círculo
//            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
//        ){
//            IconButton(
//                onClick = {
//                    val intent = Intent(context, Augmented_Reality_Activity::class.java)
//                    context.startActivity(intent)
//                    //ModelScreen()
//                },
//                modifier = Modifier.size(50.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.rayito),
//                    contentDescription = null,
//                    modifier=Modifier.size(30.dp)
//                )
//            }
//        }
//        }
//
////        }
//
//        // Mostrar texto YES/NOPE basado en el estado de deslizamiento
//        when (swipeState) {
//            SwipeDirection.ACCEPTED -> {
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopStart) // Cambiar a la esquina superior izquierda
//                        .border(2.dp, Color.Green, RoundedCornerShape(4.dp))
//                        .padding(4.dp)
//                        .alpha(
//                            (animatedOffsetX / swipeThreshold).coerceIn(
//                                0f,
//                                1f
//                            )
//                        ) // Transparencia dinámica
//                ) {
//                    Text(
//                        text = "YES",
//                        color = Color.Green,
//                        fontSize = 32.sp
//                    )
//                }
//            }
//
//            SwipeDirection.REJECTED -> {
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd) // Cambiar a la esquina superior derecha
//                        .border(2.dp, Color.Red, RoundedCornerShape(4.dp))
//                        .padding(4.dp)
//                        .alpha(
//                            (-animatedOffsetX / swipeThreshold).coerceIn(
//                                0f,
//                                1f
//                            )
//                        ) // Transparencia dinámica
//                ) {
//                    Text(
//                        text = "NOPE",
//                        color = Color.Red,
//                        fontSize = 32.sp
//                    )
//                }
//            }
//
//            else -> {
//                // No mostrar nada si está en el estado por defecto
//            }
//        }
//    }
//
//}
//@Composable
//fun ModelScreen() {
//    val context = LocalContext.current
//    val nodes = remember { mutableListOf<Node>() }
//    val coroutineScope = rememberCoroutineScope()
//    var showPopup by remember { mutableStateOf(false) }
//
//    // Define las posiciones de la cámara
//    val cameraPositions = listOf(
//        Position(0f, 1.5f, 5f), // Posición 1
//        Position(2f, 1.5f, 3f), // Posición 2
//        Position(-2f, 1.5f, 3f), // Posición 3
//        Position(0f, 2f, 1f) // Posición 4
//    )
//
//    // Estado actual de la cámara
//    var currentCameraPosition by remember { mutableStateOf(cameraPositions[0]) }
//
//    Scene(modifier = Modifier.fillMaxSize(), nodes = nodes,
//        //cameraPosition = currentCameraPosition,
//        onCreate = {
//        val model = ModelNode()
//        model.loadModelGlbAsync(
//            context = context,
//            glbFileLocation = "models/oficina.glb",
//            autoAnimate = true,
//            scaleToUnits = 0.5f,
//            centerOrigin = Position(0.0f, 0.0f, 0.0f),
//            onError = {
//                Log.e("SceneView", it.message.toString())
//            }
//        )
//        model.onTap = { hitTestResult, renderable ->
//            coroutineScope.launch {
//                showPopup = true
//                SharedStatePopUp.text_i_want_to_show.value = when (renderable) {
//                    262 -> "Monitor"
//                    163 -> "Pencil Suitcase"
//                    282 -> "iPad"
//                    242 -> "Window"
//                    274 -> "Agus"
//                    450 -> "OhhYeah"
//                    else -> ""
//                }
//                SharedStatePopUp.description.value = when (renderable) {
//                    262 -> "27-inch IPS digital monitor..."
//                    163 -> "The black pencil case..."
//                    282 -> "The iPad is a brand..."
//                    242 -> "Window film with blinds pattern..."
//                    else -> ""
//                }
//                Log.e("Object TAPPED!", "$renderable")
//            }
//        }
//
//        nodes.add(model)
//    })
//
//    // UI: Incluye los botones para mover la cámara
//    Column {
//        // Espacio para la escena
//        Box(modifier = Modifier.weight(1f)) {
//            Scene(
//                modifier = Modifier.fillMaxSize(),
//                nodes = nodes//,
//               // cameraPosition = currentCameraPosition
//            )
//        }
//
//        // Botones para cambiar la posición de la cámara
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            cameraPositions.forEachIndexed { index, position ->
//                Button(onClick = {
//                    currentCameraPosition = position
//                }) {
//                    Text(text = "Posición ${index + 1}")
//                }
//            }
//        }
//    }
//
//    // Popup
//    ShowPopup(showPopup = showPopup, onDismiss = { showPopup = false })
//}
//
//@Composable
//fun ShowPopup(showPopup: Boolean, onDismiss: () -> Unit) {
//    if (showPopup) {
//        Popup(
//            alignment = Alignment.Center,
//            onDismissRequest = onDismiss
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(200.dp)
//                    .background(Color.White)
//                    .border(1.dp, Color.Black)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)){
//                    Text(
//                        text = SharedStatePopUp.text_i_want_to_show.value,
//                        fontSize = 24.sp,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//                Text(
//                    text = SharedStatePopUp.description.value,
//                    fontSize = 10.sp
//                )}
//                Log.e("Título",SharedStatePopUp.text_i_want_to_show.value)
//                Log.e("Cuerpo",SharedStatePopUp.description.value)
//            }
//        }
//    }
//}