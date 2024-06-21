package com.example.securityquest.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FiberSmartRecord
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.FiberSmartRecord
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.TurnSharpRight
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import com.google.android.material.color.utilities.DynamicColor
import com.google.android.material.color.utilities.MaterialDynamicColors
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class LeaderboardEntry(
    val gameState: String,
    val playerName: String,
    val game: String,
    val level: Long,
    val time: Long,
    val password: String,
    val score: Long
)

@Composable
fun LeaderboardScreen(
    sortByTime: Boolean, sortByLevel: Boolean, sortByPlayerName: Boolean, showOnlyWonGames: Boolean, entryCount: Int,
    showSnake: Boolean, showTicTacToe: Boolean, showVierGewinnt: Boolean, refreshTrigger: Int, navController: NavController
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var leaderboardEntries by rememberSaveable { mutableStateOf(emptyList<LeaderboardEntry>()) }

    LaunchedEffect(refreshTrigger) {
        // Fetch and filter leaderboard data
        val allEntries = getLeaderboardData()

        // Filter by game types
        val filteredEntries = allEntries.filter { entry ->
            (showSnake && entry.game == "Snake") ||
                    (showTicTacToe && entry.game == "Tic Tac Toe") ||
                    (showVierGewinnt && entry.game == "Vier Gewinnt")
        }

        // Further filter to show only won games if required
        val wonEntries = if (showOnlyWonGames) {
            filteredEntries.filter { it.gameState == "Verloren" || it.gameState == "Unentschieden" }
        } else {
            filteredEntries
        }

        // Sort entries based on provided criteria
        val sortedEntries = when {
            sortByLevel && sortByTime && sortByPlayerName -> wonEntries.sortedWith(
                compareByDescending<LeaderboardEntry> { it.level }
                    .thenBy { it.time }
                    .thenBy { it.playerName.lowercase() }
            )
            sortByLevel && sortByTime -> wonEntries.sortedWith(
                compareByDescending<LeaderboardEntry> { it.level }
                    .thenBy { it.time }
            )
            sortByLevel && sortByPlayerName -> wonEntries.sortedWith(
                compareByDescending<LeaderboardEntry> { it.level }
                    .thenBy { it.playerName.lowercase() }
            )
            sortByTime && sortByPlayerName -> wonEntries.sortedWith(
                compareBy<LeaderboardEntry> { it.time }
                    .thenBy { it.playerName.lowercase() }
            )
            sortByLevel -> wonEntries.sortedByDescending { it.level }
            sortByTime -> wonEntries.sortedBy { it.time }
            sortByPlayerName -> wonEntries.sortedBy { it.playerName.lowercase() }
            else -> wonEntries
        }

        // Limit the number of entries
        leaderboardEntries = sortedEntries.take(entryCount)
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 400.dp)
                .fillMaxWidth()
                .wrapContentSize()
        )
    } else {
        LeaderboardList(leaderboardEntries, navController)
    }
}

@Composable
fun LeaderboardList(entries: List<LeaderboardEntry>, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(top = 70.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (entry in entries) {
            ListItem(
                headlineContent = { Text(entry.playerName) },
                overlineContent = {
                    Text(
                        entry.game,
                        color =
                            when (entry.gameState) {
                                "Gewonnen" -> MaterialTheme.colorScheme.error
                                "Verloren" -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.secondary
                            }
                    )
                },
                supportingContent = {
                    Text("Level ${entry.level}")
                },
                leadingContent = {
                    Icon(
                        when (entry.game) {
                            "Tic Tac Toe" -> Icons.Rounded.Clear
                            "Vier Gewinnt" -> Icons.Outlined.FiberSmartRecord
                            "Snake" -> Icons.Rounded.TurnSharpRight
                            else -> Icons.Rounded.Warning
                        },
                        contentDescription = "Game Icon",
                    )
                },
                trailingContent = { Text("Zeit ${entry.time / 60}:${(entry.time % 60).toString().padStart(2, '0')}")
                    if(entry.password.isNotEmpty()){
                        IconButton(onClick = { navController.navigate("startingPage/${entry.password}") }, modifier = Modifier.padding(top = 12.dp)) {
                            Icon(Icons.Rounded.Key, contentDescription = "Try")
                        }
                    }
                }
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

suspend fun getLeaderboardData(): List<LeaderboardEntry> {
    val db = Firebase.firestore
    return try {
        val result = db.collection("leaderboard").get().await()
        result.documents.map { document ->
            LeaderboardEntry(
                document.getString("Status") ?: "Kein Gewinner",
                document.getString("Name") ?: "Kein Name",
                document.getString("Spiel") ?: "Kein Spiel",
                document.getLong("Level") ?: 0,
                document.getLong("Zeit") ?: 0,
                document.getString("Passwort") ?: "",
                document.getLong("Punktzahl") ?: 0
            )
        }
    } catch (e: Exception) {
        Log.w("Firestore", "Error getting documents.", e)
        emptyList()
    }
}