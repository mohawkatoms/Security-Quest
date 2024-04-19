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
import androidx.compose.material3.LinearProgressIndicator
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

@Composable
fun BrickPage(modifier: Modifier = Modifier, navController: NavController, passwordStrength: Int) {
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
            modifier = Modifier.fillMaxSize().padding(top = 55.dp)) {
            LinearProgressIndicator(
                progress = passwordStrength/100.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
            Row {
                Text(
                    text = "Brick",
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
                        .height(480.dp)
                        .padding(16.dp), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Text(
                        text = "Brick",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(13.dp)
                    )
                    Text(
                        text = "Das ist eine Anleitung zum Spielen von Brick",
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
                        text = "Zerstöre alle Steine im Level mit einem Ball, der von einem Paddel abprallt.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Spielmechanik",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Steuere ein Paddel, um einen Ball abzufeuern und ihn in Richtung der Steine zu lenken. Der Ball prallt dabei von Wänden und dem Paddel ab.",
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
                        text = "Bewege das Paddel, um den Ball im Spiel zu halten und die Steine zu zerstören. Beende das Level, indem du alle Steine zerstörst, bevor der Ball das Paddel verlässt und ein Leben verlierst.",
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