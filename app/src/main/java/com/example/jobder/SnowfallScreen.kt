package com.example.jobder

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.*


fun generateSnowflakes(snowflakes: MutableList<Snowflake>, count: Int) {
    repeat(count) {
        snowflakes.add(Snowflake())
    }
}

@Composable
fun Snowfall(snowflakes: MutableList<Snowflake>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        snowflakes.forEach { snowflake ->
            drawCircle(
                color = Color.White,
                radius = snowflake.size,
                center = snowflake.position
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            snowflakes.forEach { it.update() }
            snowflakes.removeAll { it.position.y > 1920 }
            delay(16L)
        }
    }
}

class Snowflake {
    var position by mutableStateOf(Offset(Random.nextFloat() * 1080, Random.nextFloat() * 1920))
    val size = Random.nextFloat() * 5 + 5

    fun update() {
        position = position.copy(y = position.y + size / 2)
    }
}