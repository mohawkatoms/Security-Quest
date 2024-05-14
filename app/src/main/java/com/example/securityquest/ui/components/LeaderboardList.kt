package com.example.securityquest.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class LeaderboardEntry (
    val playerName: String,
    val game: String,
    val level: String,
    val time: String
)
@Composable
fun LeaderboardList() {

    val list = listOf(LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"), LeaderboardEntry("Jona", "Tic Tac Toe", "Level 100", "0:12"), LeaderboardEntry("Nils", "Vier Gewinnt", "Level 80", "1:12"), LeaderboardEntry("Jan", "Snake", "Level 50", "2:35"))
    Column (modifier = Modifier
        .padding(top = 70.dp)
        .verticalScroll(rememberScrollState())) {
        for (entry in list) {
            ListItem(
                headlineContent = { Text(entry.playerName) },
                overlineContent = { Text(entry.game) },
                supportingContent = { Text(entry.level) },
                leadingContent = {
                    Icon(
                        when(entry.game) {
                            "Tic Tac Toe" -> Icons.Rounded.Clear
                            "Vier Gewinnt" -> Icons.Rounded.MoreVert
                            "Snake" -> Icons.Rounded.Refresh
                            else -> {
                                Icons.Rounded.Warning
                            }
                        },
                        contentDescription = "Game Icon",
                    )
                },
                trailingContent = { Text(entry.time) }
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}