package ui.screens.swipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import ui.screens.Persona

@Composable
fun SwipeableCardPerson(item: Persona, onSwipeLeft: () -> Unit, onSwipeRight: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Aquí iría la lógica de swipeable
        // Ejemplo simple de representación
        Image(
            painter = painterResource(id = item.imagenResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Agregar el texto
        Text(
            text = item.nombre,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Text(
            text = item.cargo,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = item.descripcion,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}