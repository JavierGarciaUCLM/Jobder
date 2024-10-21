package ui.screens.swipe

import androidx.compose.runtime.Composable

@Composable
fun SwipeableMainScreen() {
    val companies = listOf("Company A", "Company B", "Company C", "Company D")

    SwipeableCards(companies = companies)
}