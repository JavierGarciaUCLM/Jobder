package ui.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.jobder.R
import kotlinx.coroutines.launch
import ui.screens.swipe.SwipeableCard
import ui.screens.swipe.SwipeableCardPerson

data class Persona(
    val imagenResId: Int,
    val nombre: String,
    val cargo: String,
    val descripcion: String
)

@Composable
fun SwipeableCardsScreenPerson() {
    val personas = remember {
        mutableStateListOf(
            Persona(R.drawable.persona1, "Juan Pérez", "Desarrollador", "Juan es un experto en desarrollo web."),
            Persona(R.drawable.persona2, "Ana Gómez", "Diseñadora", "Ana tiene una gran experiencia en diseño gráfico."),
            Persona(R.drawable.persona3, "Carlos Ruiz", "Gerente de Proyecto", "Carlos gestiona proyectos con gran efectividad."),
            Persona(R.drawable.persona4, "Luisa Fernández", "Analista de Datos", "Luisa es especialista en análisis de datos."),
            Persona(R.drawable.persona5, "Gonzalo Mínguez", "Analista de Datos", "Gonzalo es especialista en análisis de datos.")
        )
    }
    val coroutineScope = rememberCoroutineScope()

    fun removePersona() {
        if (personas.isNotEmpty()) {
            personas.removeAt(0)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (personas.isNotEmpty()) {
            SwipeableCardPerson(
                item = personas.first(),
                onSwipeLeft = { coroutineScope.launch { removePersona() } },
                onSwipeRight = { coroutineScope.launch { removePersona() } }
            )
        } else {
            Text(text = "No more people", fontSize = 24.sp)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MaterialTheme {
//        SwipeableCardsScreen()
//    }
//}