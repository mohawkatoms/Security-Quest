package com.example.securityquest.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.securityquest.ui.components.AnimatedLinearProgressIndicator
import com.example.securityquest.ui.components.TicTacToe

@Composable
fun TicTacToePage(modifier: Modifier = Modifier, navController: NavController, passwordStrength: Int) {
    Box(modifier) {
        var isReturnDialogOpen by rememberSaveable {
            mutableStateOf(false)
        }
        var isExplanationDialogOpen by rememberSaveable {
            mutableStateOf(false)
        }
        Row(horizontalArrangement = Arrangement.End) {
            FilledIconToggleButton(
                checked = isReturnDialogOpen,
                onCheckedChange = { isReturnDialogOpen = true },
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Go Back")
            }
            Spacer(Modifier.weight(1f))
            FilledIconToggleButton(
                checked = isExplanationDialogOpen,
                onCheckedChange = { isExplanationDialogOpen = true },
                modifier = Modifier.padding(end = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Rounded.List, contentDescription = "Explanation")
            }
        }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 55.dp)) {
            AnimatedLinearProgressIndicator(indicatorProgress = passwordStrength/100.toFloat())
            Row {
                Text(
                    text = "Tic Tac Toe",
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Stärke $passwordStrength",
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(end = 5.dp, top = 5.dp)
                )
            }
        }
        TicTacToe()
        if (isReturnDialogOpen) {
            Dialog(onDismissRequest = { isReturnDialogOpen = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Text(
                        text = "Spiel verlassen",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(13.dp)
                    )
                    Text(
                        text = "Sind Sie sicher, dass sie das Spiel verlassen möchten? Der Fortschritt geht dabei verloren.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp)
                    )
                    Row {
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { isReturnDialogOpen = false }) {
                            Text(text = "Fortsetzen")
                        }
                        TextButton(onClick = {
                            isReturnDialogOpen = false
                            navController.popBackStack()
                        }) {
                            Text(text = "Verlassen")
                        }
                    }
                }
            }
        }
        if(isExplanationDialogOpen) {
            Dialog(onDismissRequest = { isExplanationDialogOpen = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(16.dp), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Text(
                        text = "Tic Tac Toe",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(13.dp)
                    )
                    Text(
                        text = "Das ist eine Anleitung zum Spielen von Tic Tac Toe",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Ziel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Gewinne, indem du als Erster eine Reihe von drei gleichen Symbolen horizontal, vertikal oder diagonal bildest.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Spielablauf",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Spieler 1 beginnt, indem er sein Symbol in ein leeres Feld setzt. Spieler 2 platziert dann sein Symbol in einem beliebigen leerem Feld. Spieler wechseln sich ab, bis das Feld voll ist (Unentschieden). ",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Spielfeld",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Zum Spielen dient ein 3x3 Raster mit neun Feldern.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Row {
                        Spacer(Modifier.weight(1f))
                        TextButton(
                            onClick = { isExplanationDialogOpen = false },
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Text(text = "Schließen")
                        }
                    }
                }
            }
        }
    }
}