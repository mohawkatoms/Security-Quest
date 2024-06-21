package com.example.securityquest.ui.page.resultpage

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.securityquest.ui.components.FabItem
import com.example.securityquest.ui.components.FilteredOutlinedTextField
import com.example.securityquest.ui.components.MultiFloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("RestrictedApi")
@Composable
fun SnakeResultPage(
    score: Int,
    navController: NavController,
    passwordStrength: Int,
    time: Long,
    password: String,
    points: Int
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Box {
        var isLeaderboardDialogOpen by rememberSaveable { mutableStateOf(false) }
        var playerName by rememberSaveable { mutableStateOf("") }
        var alreadyPublishedEntry by rememberSaveable { mutableStateOf(false) }
        var publishPassword by rememberSaveable { mutableStateOf(false) }
        val pointsToWin = 3 + (27 * (passwordStrength - 1) / 99)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val icon = if (score == 1) {
                Icons.Outlined.LockOpen
            } else {
                Icons.Outlined.Lock
            }
            Icon(icon, contentDescription = "Copy", Modifier.size(120.dp), tint = MaterialTheme.colorScheme.secondary)
            Text(
                text = if(score == 1) {
                    "Du hast das Passwort geknackt"
                } else {
                    "Das Passwort war zu sicher"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = when (score) {
                    1 -> MaterialTheme.colorScheme.error
                    0 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary },
                modifier = Modifier.padding(bottom = 8.dp, top = 90.dp)
            )
            Text(
                text = when (score) {
                    1 -> "Versuche es lieber mit einem stärkeren Passwort"
                    0 -> "Das Passwort schützt dich zuverlässig"
                    else -> {"Das Passwort ist sicher"}
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                Text(
                    text = "Das Passwort: $password",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = { clipboardManager.setText(AnnotatedString(password)) }) {
                    Icon(Icons.Rounded.ContentCopy, contentDescription = "Copy")
                }
            }
            Text(
                text = "Stärke des Passworts: $passwordStrength",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Erreichte Punkte: $points/$pointsToWin",
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
                    navController.navigate("startingPage") {
                        popUpTo("startingPage") { inclusive = true }
                    }
                    navController.navigate("snakePage/$passwordStrength/$password")

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

            Row {
                MultiFloatingActionButton(
                    fabIcon = Icons.Rounded.Leaderboard,
                    items = listOf(
                        FabItem(
                            Icons.Rounded.EmojiEvents,
                            "Leaderboard anzeigen",
                            onFabItemClicked = { navController.navigate("leaderboardPage") }),
                        FabItem(Icons.Rounded.Add, "Eintrag erstellen", onFabItemClicked = { isLeaderboardDialogOpen = true })
                    )
                )
            }

            if (isLeaderboardDialogOpen) {
                Dialog(onDismissRequest = { isLeaderboardDialogOpen = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(565.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            text = "Ergebnis Speichern",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(13.dp)
                        )
                        Text(
                            text = "Hier können Sie Ihr Ergebnis speichern, um es anderen Spielern zu zeigen.",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp)
                        )
                        Text(
                            text = "Spiel",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "Snake",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Deine Zeit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "${time / 60}:${(time % 60).toString().padStart(2, '0')}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Level",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "Level $passwordStrength",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Punkte",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "$points/$pointsToWin",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Ergebnis",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = if(score == 1) {"Geknackt"} else {"Nicht Geknackt"},
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Dein Name",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        FilteredOutlinedTextField(
                            text = playerName,
                            onChanged = { playerName = it },
                            ignoredRegex = Regex("[^a-zA-Z0-9]"),
                            "Name",
                            modifier = Modifier.padding(start = 13.dp, end = 13.dp, top = 5.dp)
                        )
                        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 7.dp, start = 13.dp, end = 13.dp)){
                            Text(
                                text = "Passwort veröffentlichen",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = publishPassword, onCheckedChange = {publishPassword = it})
                        }
                        Row {
                            Spacer(Modifier.weight(1f))
                            TextButton(onClick = { isLeaderboardDialogOpen = false }) {
                                Text(text = "Verlassen")
                            }
                            TextButton(onClick = {
                                isLeaderboardDialogOpen = false
                                scope.launch {
                                    val success = addEntry(passwordStrength, playerName, score, time, publishPassword, password)
                                    if (success) {
                                        snackbarHostState.showSnackbar("Eintrag erfolgreich hinzugefügt!")
                                    } else {
                                        snackbarHostState.showSnackbar("Fehler beim Hinzufügen des Eintrags.")
                                    }
                                }
                                alreadyPublishedEntry = true
                            }, enabled = playerName.length > 2 && !alreadyPublishedEntry) {
                                Text(text = "Speichern")
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

private suspend fun addEntry(passwordStrength: Int, playerName: String, score: Int, time: Long, publishPassword: Boolean, password: String): Boolean {
    val db = Firebase.firestore
    val entry = hashMapOf(
        "Spiel" to "Snake",
        "Level" to passwordStrength,
        "Name" to playerName,
        "Status" to if (score == 1) "Gewonnen" else "Verloren",
        "Zeit" to time,
        "Passwort" to if (publishPassword) password else null
    )

    return try {
        db.collection("leaderboard")
            .add(entry)
            .await()
        Log.d(TAG, "DocumentSnapshot was added to the Database")
        true
    } catch (e: Exception) {
        Log.w(TAG, "Error adding document", e)
        false
    }
}