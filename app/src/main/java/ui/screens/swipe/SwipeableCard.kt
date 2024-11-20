package ui.screens.swipe
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define los posibles estados de la tarjeta
enum class SwipeDirection {
    DEFAULT, ACCEPTED, REJECTED
}

// Tarjeta individual deslizante
@Composable
fun SwipeableCard(
    item: Int, // Cambiado a Int para el ID del recurso de la imagen
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Variable para el desplazamiento de la tarjeta
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 20f // Reducir el umbral para detectar el gesto de deslizamiento

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
            .height(300.dp)
            .graphicsLayer(
                translationX = animatedOffsetX, // Mueve la tarjeta con animación
                rotationZ = rotationDegree // Aplica la rotación a la tarjeta
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
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
        // Contenido de la tarjeta
        Image(
            painter = painterResource(id = item),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

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