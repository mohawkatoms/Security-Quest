package com.example.securityquest.ui.page.tictactoeresult

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TicTacToeResultPage(
    status: String,
    navController: NavController,
    passwordStrength: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Text displaying result
        val text = when (status) {
            "X" -> "You won!"
            "O" -> "You lost!"
            "D" -> "It's a draw!"
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
            text = "Opponent's Strength: $passwordStrength",
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
            Text(text = "Replay")
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
            Text(text = "Home")
        }
    }
}