package ui.screens

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.jobder.LoginScreen
import com.example.jobder.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ui.screens.swipe.SwipeableCard

object SharedSwipes {
    var item: Int = 0
    private val job = Job() // Define un Job para controlar el ciclo de vida
    private val scope = CoroutineScope(Dispatchers.Main + job) // Usa Dispatchers.Main o el que prefieras
     var modifier: Modifier? = null
var isPersona = false
    var onSwipeLeft: (() -> Unit)? = null
    var onSwipeRight: (() -> Unit)? = null

    fun performSwipeLeft(action: suspend () -> Unit) {
        onSwipeLeft = {
            scope.launch {
                action()
            }
        }
    }

    fun performSwipeRight(action: suspend () -> Unit) {
        onSwipeRight = {
            scope.launch {
                action()
            }
        }
    }

    fun clear() {
        job.cancel() // Cancela el scope asociado
    }
}
@Composable
fun SwipeableCardsScreen() {
    val companies = remember { mutableStateListOf(
        R.drawable.company1,
        R.drawable.company2,
        R.drawable.company3,
        R.drawable.company4,
        R.drawable.company5
    ) }
    val coroutineScope = rememberCoroutineScope()

    // Función para eliminar una tarjeta de la lista
    fun removeCompany() {
        if (companies.isNotEmpty()) {
            companies.removeAt(0)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (companies.isNotEmpty()) {
            // Mostrar la tarjeta actual en la parte superior
            val context = LocalContext.current
            SharedSwipes.item = companies.first()
            SharedSwipes.modifier = Modifier
            SharedSwipes.isPersona = false

            val intent = Intent(context, SwipeableCard::class.java)


            context.startActivity(intent)
//            SwipeableCard(
//                item = companies.first(),
//                onSwipeLeft = { SharedSwipes.onSwipeLeft?.invoke() },
//                onSwipeRight = { SharedSwipes.onSwipeRight?.invoke()  }
//            )
        } else {
            //Text(text = "No more companies", fontSize = 24.sp)
//            Image(
//                modifier = Modifier
//                    .fillMaxSize()
//                , painter = painterResource(id = R.drawable.matchUCLM)
//            )
            Image(
                painter = painterResource(id=R.drawable.matchuclm),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
                )
            //painterResource(id = R.drawable.matchuclm)
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