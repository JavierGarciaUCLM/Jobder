package ui.screens.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun SwipeableCards(companies: List<String>) {
    var currentIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    var swipeResult by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (currentIndex < companies.size) {
            Box(
                modifier = Modifier
                    .size(300.dp, 400.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 100) {
                                // Swipe to the right (YES)
                                scope.launch {
                                    swipeResult = "YES"
                                    currentIndex++
                                }
                            } else if (dragAmount < -100) {
                                // Swipe to the left (NOPE)
                                scope.launch {
                                    swipeResult = "NOPE"
                                    currentIndex++
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = companies[currentIndex],
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            if (swipeResult.isNotEmpty()) {
                Text(
                    text = swipeResult,
                    color = if (swipeResult == "YES") Color.Green else Color.Red,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp)
                )
            }
        } else {
            Text(
                text = "No more companies!",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}