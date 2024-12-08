package ui.screens.swipe
import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat.startActivity
import com.example.jobder.Augmented_Reality_Activity
import com.example.jobder.NewLoginScreen
import com.example.jobder.R
import com.example.jobder.SharedState
import com.google.ar.sceneform.CameraNode
import io.github.sceneview.Scene
import io.github.sceneview.SceneView
import io.github.sceneview.ar.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.utils.TAG
import kotlinx.coroutines.launch

// Define los posibles estados de la tarjeta
enum class SwipeDirection {
    DEFAULT, ACCEPTED, REJECTED
}
object SharedStatePopUp {
    var text_i_want_to_show = mutableStateOf("")
    var description = mutableStateOf("")
    var position = mutableStateOf(Position(0f,0f,0f))
}

// Tarjeta individual deslizante
@Composable
fun SwipeableCard(
    item: Int, // Cambiado a Int para el ID del recurso de la imagen
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Variable para el desplazamiento de la tarjeta
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 20f // Reducir el umbral para detectar el gesto de deslizamiento
    //var isRejectedPressed by remember { mutableStateOf(false) }
    //var isAcceptedPressed by remember { mutableStateOf(false) }
    // Animar el desplazamiento de la tarjeta para una transición suave
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    // Agregar rotación mientras se desliza la tarjeta
    val rotationDegree by remember {
        derivedStateOf {
            (animatedOffsetX / 30).coerceIn(-20f, 20f) // Limitar la rotación
        }
    }

    // Usa remember para optimizar la recomposición de este estado derivado
    val swipeState by remember {
        derivedStateOf {
            when {
                animatedOffsetX > swipeThreshold -> SwipeDirection.ACCEPTED // Deslizado a la derecha
                animatedOffsetX < -swipeThreshold -> SwipeDirection.REJECTED // Deslizado a la izquierda
                else -> SwipeDirection.DEFAULT // En el centro o por defecto
            }
        }
    }

    Box(
        modifier = modifier

            .fillMaxSize()

            .clip(RoundedCornerShape(10.dp))
            .graphicsLayer(
                translationX = animatedOffsetX, // Mueve la tarjeta con animación
                rotationZ = rotationDegree // Aplica la rotación a la tarjeta
            )
            .background(SharedState.theme.value.background, shape = RoundedCornerShape(10.dp))
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta * 2 // Aumentar la sensibilidad del arrastre
                },
                onDragStopped = {
                    when (swipeState) {
                        SwipeDirection.ACCEPTED -> {
                            onSwipeRight()
                            offsetX = 0f // Reinicia la posición
                        }

                        SwipeDirection.REJECTED -> {
                            onSwipeLeft()
                            offsetX = 0f // Reinicia la posición
                        }

                        else -> {
                            // Vuelve al centro más lentamente
                            offsetX = animatedOffsetX * 0.5f
                        }
                    }
                }
            )
            .padding(16.dp)
    ) {
        val logo = if (SharedState.darkModeIsChecked.value) {
            painterResource(id = R.mipmap.ic_launcher_foreground) // Logo en modo oscuro
        } else {
            painterResource(id = R.mipmap.img) // Logo en modo claro
        }

        //Spacer(modifier = Modifier.height(16.dp))
        // Contenido de la tarjeta
        Image(
            painter = painterResource(id = item),
            contentDescription = null,
            modifier =
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50.dp))
                //.align(Alignment.Center)



        )
//        Row(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//        ) {
            IconButton(
               onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    //.align(Alignment.TopEnd)
                    .padding(top = 10.dp)

            ){
                Image(
                    painter = painterResource(id = R.drawable.topstart),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null
                )
            }
            IconButton(

                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp),
                onClick = {}
            ){
                Image(
                    logo,contentDescription = null
                )
            }
                IconButton(
                    onClick = {},
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp)
            ){ Image(
                    painter = painterResource(id = R.drawable.chat),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null
                )
            }
        Row(modifier = Modifier.align(Alignment.BottomCenter)){
        Box(
            modifier = Modifier
                .size(70.dp) // Tamaño del círculo
                .clip(CircleShape) // Hace la caja circular
                .background(Color.White)
                .padding(10.dp)
                ,//.align(Alignment.BottomStart), // Fondo del círculo
            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
        ){
            IconButton(
                onClick = {},
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.volver),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(70.dp) // Tamaño del círculo
                .clip(CircleShape) // Hace la caja circular
                .background(Color.White)
                .padding(10.dp)
                ,//.align(Alignment.BottomCenter), // Fondo del círculo
            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
        ){
            IconButton(
                onClick = {
                    //isRejectedPressed = true
                },
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rechazar),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)) }
        }
        Box(
            modifier = Modifier
                .size(70.dp) // Tamaño del círculo
                .clip(CircleShape) // Hace la caja circular
                .background(Color.White)
                .padding(10.dp)
                ,//.align(Alignment.BottomCenter), // Fondo del círculo
            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
        ){
            IconButton(
                onClick = {},
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.estrella),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                ) }
        }
        Box(
            modifier = Modifier
                .size(70.dp) // Tamaño del círculo
                .clip(CircleShape) // Hace la caja circular
                .background(Color.White)
                .padding(10.dp)
                ,//.align(Alignment.BottomCenter), // Fondo del círculo
            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
        ){
            IconButton(
                onClick = {
                    //isAcceptedPressed = true
                    //coroutine.Launc{removePersona()}
                },
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.corazoncito),
                    contentDescription = null,
                    modifier=Modifier.size(30.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(70.dp) // Tamaño del círculo
                .clip(CircleShape) // Hace la caja circular
                .background(Color.White)
                .padding(10.dp)
                ,//.align(Alignment.BottomEnd), // Fondo del círculo
            contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
        ){
            IconButton(
                onClick = {
                    val intent = Intent(context, Augmented_Reality_Activity::class.java)
                    context.startActivity(intent)
                    //ModelScreen()
                },
                modifier = Modifier.size(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rayito),
                    contentDescription = null,
                    modifier=Modifier.size(30.dp)
                )
            }
        }
        }

//        }

        // Mostrar texto YES/NOPE basado en el estado de deslizamiento
        when (swipeState) {
            SwipeDirection.ACCEPTED -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart) // Cambiar a la esquina superior izquierda
                        .border(2.dp, Color.Green, RoundedCornerShape(4.dp))
                        .padding(4.dp)
                        .alpha(
                            (animatedOffsetX / swipeThreshold).coerceIn(
                                0f,
                                1f
                            )
                        ) // Transparencia dinámica
                ) {
                    Text(
                        text = "YES",
                        color = Color.Green,
                        fontSize = 32.sp
                    )
                }
            }

            SwipeDirection.REJECTED -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Cambiar a la esquina superior derecha
                        .border(2.dp, Color.Red, RoundedCornerShape(4.dp))
                        .padding(4.dp)
                        .alpha(
                            (-animatedOffsetX / swipeThreshold).coerceIn(
                                0f,
                                1f
                            )
                        ) // Transparencia dinámica
                ) {
                    Text(
                        text = "NOPE",
                        color = Color.Red,
                        fontSize = 32.sp
                    )
                }
            }

            else -> {
                // No mostrar nada si está en el estado por defecto
            }
        }
    }

}
@Composable
fun ModelScreen() {
    val context = LocalContext.current
    val nodes = remember { mutableListOf<Node>() }
    val coroutineScope = rememberCoroutineScope()
    var showPopup by remember { mutableStateOf(false) }
    var sceneReference by remember { mutableStateOf<SceneView?>(null) }
    var cameraNode by remember { mutableStateOf<CameraNode?>(null) }

    // Define las posiciones de la cámara
    val cameraPositions = listOf(
        Position(0f, 1.5f, 5f),  // Posición 1
        Position(2f, 1.5f, 3f),  // Posición 2
        Position(-2f, 1.5f, 3f)  // Posición 3
    )

    // Renderizar la escena
    Scene(
        modifier = Modifier.fillMaxSize(),
        nodes = nodes,
        onCreate = { scene ->
            sceneReference = scene

            // Acceder al nodo de cámara existente y configurarlo
//            val camera = CameraNode(engine).apply {
//                position = cameraPositions[0] // Posición inicial
//                parent = scene // Establecer como parte de la escena
//                //position = cameraPositions[0]
//            }
            //cameraNode = camera // Guardar referencia local para modificarla más adelante

            // Cargar el modelo 3D
            val model = ModelNode()
            model.loadModelGlbAsync(
                context = context,
                glbFileLocation = "models/oficina.glb",
                autoAnimate = true,
                scaleToUnits = 0.5f,
                centerOrigin = Position(0f, 0f, 0f), // Centrar el modelo
                onError = { Log.e("SceneView", it.message.toString()) }
            )
            nodes.add(model)
        }
    )

    // Interfaz de usuario: Botones para cambiar la posición de la cámara
    Column {
        // Espacio para la escena
        Box(modifier = Modifier.weight(1f)) {
            Scene(
                modifier = Modifier.fillMaxSize(),
                nodes = nodes
            )
        }

        // Botones para mover la cámara
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            cameraPositions.forEachIndexed { index, position ->
                Button(onClick = {
                    sceneReference?.cameraNode.apply { this?.position = position }

                    Log.d("Camera", "Camera position changed to: $position")
                }) {
                    Text(text = "Posición ${index + 1}")
                }
            }
        }
    }

    // Mostrar el popup si es necesario
    ShowPopup(showPopup = showPopup, onDismiss = { showPopup = false })
}

@Composable
fun ShowPopup(showPopup: Boolean, onDismiss: () -> Unit) {
    if (showPopup) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = onDismiss
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Black)
            ) {
                Column(modifier = Modifier.padding(16.dp)){
                    Text(
                        SharedStatePopUp.text_i_want_to_show.value,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                Text(
                    text = SharedStatePopUp.description.value,
                    fontSize = 10.sp
                )}
            }
        }
    }
}