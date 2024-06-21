package com.example.securityquest.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.securityquest.ui.components.LeaderboardScreen

@Composable
fun LeaderboardPage(navController: NavController) {
    var isReturnDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isFilterDialogOpen by rememberSaveable { mutableStateOf(false) }
    var sortByTime by rememberSaveable { mutableStateOf(false) }
    var sortByLevel by rememberSaveable { mutableStateOf(false) }
    var sortByPlayerName by rememberSaveable { mutableStateOf(false) }
    var showOnlyWonGames by rememberSaveable { mutableStateOf(false) }
    var entryCount by rememberSaveable { mutableIntStateOf(10) }
    val (showTicTacToe, onStateChangeTicTacToe) = remember { mutableStateOf(true) }
    val (showVierGewinnt, onStateChangeVierGewinnt) = remember { mutableStateOf(true) }
    val (showSnake, onStateChangeSnake) = remember { mutableStateOf(true) }
    var refreshTrigger by rememberSaveable { mutableIntStateOf(0) } // State to trigger refresh

    Box {
        Row(horizontalArrangement = Arrangement.End) {
            FilledIconToggleButton(
                checked = isReturnDialogOpen,
                onCheckedChange = { isReturnDialogOpen = true },
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Reply, contentDescription = "Go Back")
            }
            Spacer(Modifier.weight(1f))
            FilledIconToggleButton(
                checked = isFilterDialogOpen,
                onCheckedChange = { isFilterDialogOpen = true },
                modifier = Modifier.padding(end = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.FilterAlt, contentDescription = "Explanation")
            }
        }
        LeaderboardScreen(
            sortByTime, sortByLevel, sortByPlayerName, showOnlyWonGames, entryCount,
            showSnake, showTicTacToe, showVierGewinnt, refreshTrigger, navController
        )
        if (isReturnDialogOpen) {
            ReturnDialog(
                onCancel = { isReturnDialogOpen = false },
                onLeave = {
                    isReturnDialogOpen = false
                    navController.popBackStack()
                }
            )
        }
        if (isFilterDialogOpen) {
            FilterDialog(
                sortByTime, sortByLevel, sortByPlayerName, showOnlyWonGames, entryCount,
                showSnake, showTicTacToe, showVierGewinnt,
                onApply = {
                    isFilterDialogOpen = false
                    refreshTrigger++ // Increment to trigger refresh
                },
                onDismiss = { isFilterDialogOpen = false },
                onStateChangeTicTacToe, onStateChangeVierGewinnt, onStateChangeSnake,
                { sortByLevel = it }, { sortByTime = it }, { sortByPlayerName = it }, { showOnlyWonGames = it }, { entryCount = it }
            )
        }
    }
}

@Composable
fun ReturnDialog(onCancel: () -> Unit, onLeave: () -> Unit) {
    Dialog(onDismissRequest = { onCancel() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(16.dp), shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Text(
                text = "Leaderboard verlassen",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(13.dp)
            )
            Text(
                text = "Sind Sie sicher, dass sie das Leaderboard verlassen möchten?",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 13.dp, end = 13.dp)
            )
            Row {
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { onCancel() }) {
                    Text(text = "Abbrechen")
                }
                TextButton(onClick = { onLeave() }) {
                    Text(text = "Verlassen")
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    sortByTime: Boolean, sortByLevel: Boolean, sortByPlayerName: Boolean, showOnlyWonGames: Boolean, entryCount: Int,
    showSnake: Boolean, showTicTacToe: Boolean, showVierGewinnt: Boolean,
    onApply: () -> Unit, onDismiss: () -> Unit,
    onStateChangeTicTacToe: (Boolean) -> Unit, onStateChangeVierGewinnt: (Boolean) -> Unit, onStateChangeSnake: (Boolean) -> Unit,
    onSortByLevelChange: (Boolean) -> Unit, onSortByTimeChange: (Boolean) -> Unit, onSortByPlayerNameChange: (Boolean) -> Unit, onShowOnlyWonGamesChange: (Boolean) -> Unit, onEntryCountChange: (Int) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(612.dp)
                .padding(16.dp), shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Text(
                text = "Leaderboard",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(13.dp)
            )
            Text(
                text = "Hier kannst du einstellen, welche Einträge angezeigt, und wie sie sortiert werden sollen",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                lineHeight = 19.sp
            )
            Text(
                text = "Anzahl der Einträge $entryCount",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 13.dp, top = 10.dp)
            )
            Row(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                Slider(
                    value = entryCount.toFloat(),
                    onValueChange = { onEntryCountChange(it.toInt()) },
                    valueRange = 10f..100f,
                    steps = 17
                )
            }
            Row {
                Text(
                    text = "Nach Level",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                )
                Text(
                    text = "Nach Zeit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 70.dp, top = 10.dp)
                )
            }
            Row(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                Switch(
                    checked = sortByLevel,
                    onCheckedChange = { onSortByLevelChange(it) }
                )
                Switch(
                    modifier = Modifier.padding(start = 92.dp),
                    checked = sortByTime,
                    onCheckedChange = { onSortByTimeChange(it) }
                )
            }
            Row {
                Text(
                    text = "Nach Spieler",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                )
                Text(
                    text = "Nicht Geknackt",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 58.dp, top = 10.dp)
                )
            }
            Row(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                Switch(
                    checked = sortByPlayerName,
                    onCheckedChange = { onSortByPlayerNameChange(it) }
                )
                Switch(
                    modifier = Modifier.padding(start = 92.dp),
                    checked = showOnlyWonGames,
                    onCheckedChange = { onShowOnlyWonGamesChange(it) }
                )
            }
            Row {
                Text(
                    text = "Spiele",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                )
            }
            GameTypeCheckboxRow("Tic Tac Toe", showTicTacToe, onStateChangeTicTacToe)
            GameTypeCheckboxRow("Vier Gewinnt", showVierGewinnt, onStateChangeVierGewinnt)
            GameTypeCheckboxRow("Snake", showSnake, onStateChangeSnake)
            Row {
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Abbrechen")
                }
                TextButton(enabled = (showSnake || showTicTacToe || showVierGewinnt), onClick = { onApply() }) {
                    Text(text = "Anwenden")
                }
            }
        }
    }
}

@Composable
fun GameTypeCheckboxRow(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .toggleable(
                value = isChecked,
                onValueChange = { onCheckedChange(!isChecked) },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
