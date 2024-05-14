package com.example.securityquest.ui.page.resultpage

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.securityquest.ui.components.FabItem
import com.example.securityquest.ui.components.MultiFloatingActionButton

@Composable
fun TicTacToeResultPage(
    status: String,
    navController: NavController,
    passwordStrength: Int,
    time: Long
) {
    Box (){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, top = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Text displaying result
            val text = when (status) {
                "X" -> "Du hast gewonnen!"
                "O" -> "Du hast verloren!"
                "D" -> "Unentschieden!"
                else -> ""
            }
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Text displaying opponent's strength
            Text(
                text = "Stärke deines Passworts: $passwordStrength",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Deine Zeit: ${time / 60}:${(time % 60).toString().padStart(2, '0')}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Animated buttons
            var scale = 1f
            val scaleAnimation = remember { Animatable(1f) }
            LaunchedEffect(Unit) {
                scaleAnimation.animateTo(
                    targetValue = 1.1f,
                    animationSpec = tween(durationMillis = 500)
                ) {
                    scale = value
                }
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .scale(scale),
                shape = CircleShape
            ) {
                Text(text = "Nochmal")
            }

            Button(
                onClick = {
                    navController.navigate("startingPage")
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .scale(scale),
                shape = CircleShape
            ) {
                Text(text = "Startseite")
            }
            Row() {
                MultiFloatingActionButton(
                    fabIcon = Icons.Rounded.Star,
                    items = listOf(
                        FabItem(
                            Icons.Rounded.Menu,
                            "Leaderboard anzeigen",
                            onFabItemClicked = {}),
                        FabItem(Icons.Rounded.Create, "Eintrag erstellen", onFabItemClicked = {})
                    )
                )
            }
        }
    }
}