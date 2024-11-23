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
import androidx.compose.ui.layout.ContentScale
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
            R.drawable.persona1,
            R.drawable.persona2,
            R.drawable.persona5,
            R.drawable.persona4,
            R.drawable.persona3,
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
            SwipeableCard(
                item = personas.first(),
                onSwipeLeft = { coroutineScope.launch { removePersona() } },
                onSwipeRight = { coroutineScope.launch { removePersona() } }
            )
        } else {
            //Text(text = "No more people", fontSize = 24.sp)
            Image(
                painter = painterResource(id=R.drawable.matchjaime),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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