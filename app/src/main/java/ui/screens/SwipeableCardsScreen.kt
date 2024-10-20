package ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ui.screens.swipe.SwipeableCard
import java.lang.reflect.Modifier

@Composable
fun SwipeableCardsScreen() {
    val companies = remember { mutableStateListOf("Company 1", "Company 2", "Company 3", "Company 4") }
    val coroutineScope = rememberCoroutineScope()

    // Función para eliminar una tarjeta de la lista
    fun removeCompany() {
        if (companies.isNotEmpty()) {
            companies.removeAt(0)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        //modifier = Modifier.fillMaxSize() //está dando error no sé por qué
    ) {
        if (companies.isNotEmpty()) {
            // Mostrar la tarjeta actual en la parte superior
            SwipeableCard(
                item = companies.first(),
                onSwipeLeft = { coroutineScope.launch { removeCompany() } },
                onSwipeRight = { coroutineScope.launch { removeCompany() } }
            )
        } else {
            Text(text = "No more companies", fontSize = 24.sp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        SwipeableCardsScreen()
    }
}