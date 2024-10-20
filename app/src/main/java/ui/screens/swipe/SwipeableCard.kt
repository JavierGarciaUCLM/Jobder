package ui.screens.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// Define los posibles estados de la tarjeta
enum class SwipeDirection {
    DEFAULT, ACCEPTED, REJECTED
}

// Tarjeta individual deslizante
@Composable
fun SwipeableCard(
    item: String,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Variable para el desplazamiento de la tarjeta
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 200f // Umbral para detectar el gesto de deslizamiento
    val swipeState by remember {
        derivedStateOf {
            when {
                offsetX > swipeThreshold -> SwipeDirection.ACCEPTED // Deslizado a la derecha
                offsetX < -swipeThreshold -> SwipeDirection.REJECTED // Deslizado a la izquierda
                else -> SwipeDirection.DEFAULT // En el centro o por defecto
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                },
                onDragStopped = {
                    when (swipeState) {
                        SwipeDirection.ACCEPTED -> {
                            onSwipeRight()
                        }
                        SwipeDirection.REJECTED -> {
                            onSwipeLeft()
                        }
                        else -> {
                            // Vuelve al centro si no alcanza el umbral
                            offsetX = 0f
                        }
                    }
                }
            )
            .padding(16.dp)
    ) {
        // Contenido de la tarjeta
        Text(text = item, fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))

        // Mostrar texto YES/NOPE basado en el estado de deslizamiento
        when (swipeState) {
            SwipeDirection.ACCEPTED -> {
                Text(
                    text = "YES",
                    color = Color.Green,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .alpha(0.8f)
                )
            }
            SwipeDirection.REJECTED -> {
                Text(
                    text = "NOPE",
                    color = Color.Red,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .alpha(0.8f)
                )
            }
            else -> {
                // No mostrar nada si está en el estado por defecto
            }
        }
    }
}