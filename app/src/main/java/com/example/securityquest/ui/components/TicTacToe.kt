package com.example.securityquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun TicTacToe() {
    Box(modifier = Modifier.padding(top = 250.dp)) {
        val board = remember {
            mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
        }
        val winner = remember {
            mutableStateOf<String?>(null)
        }

        val currentPlayer = remember {
            mutableStateOf("X")
        }
        val initialBoard = Array(3) { arrayOfNulls<String>(3) }
        val initialPlayer = "X"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()) {
                Column {
                    for (row in 0..2) {
                        Row {
                            for (col in 0..2) {
                                LargeFloatingActionButton(
                                    onClick = {
                                        if (board.value[row][col] == null && winner.value == null) {
                                            board.value[row][col] = currentPlayer.value
                                            currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
                                            winner.value = checkForWinner(board.value)
                                            if (winner.value == null && isBoardFull(board.value)) {
                                                winner.value = "Draw"
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        text = board.value[row][col] ?: "",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = "Current Player: ${currentPlayer.value}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    if (winner.value != null) {
                        Text(
                            text = "Winner: ${winner.value}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(4.dp)
                        )

                        LaunchedEffect(true) {
                            delay(2000)
                            board.value = initialBoard
                            currentPlayer.value = initialPlayer
                            winner.value = null
                        }
                    }
                    Button(
                        onClick = {
                            board.value = initialBoard
                            currentPlayer.value = initialPlayer
                            winner.value = null
                        }
                    ) {
                        Text(text = "Reset")
                    }
                }
            }
        }
    }
}

fun checkForWinner(board: Array<Array<String?>>): String? {
    for (row in 0..2) {
        if (board[row][0] != null && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
            return board[row][0]
        }
    }
    for (col in 0..2) {
        if (board[0][col] != null && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
            return board[0][col]
        }
    }
    if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
        return board[0][0]
    }
    if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
        return board[0][2]
    }
    return null
}

fun isBoardFull(board: Array<Array<String?>>): Boolean {
    for (row in board) {
        for (cell in row) {
            if (cell == null) {
                return false
            }
        }
    }
    return true
}
