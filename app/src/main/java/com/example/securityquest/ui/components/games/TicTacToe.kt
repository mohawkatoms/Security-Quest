import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

@Composable
fun TicTacToe(
    onNavigateToTicTacToeResultPage: (String, Int, Long, String) -> Unit,
    passwordStrength: Int,
    password: String
) {
    Box(modifier = Modifier.padding(top = 160.dp)) {
        val board = remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
        val winner = remember { mutableStateOf<String?>(null) }
        val currentPlayer = remember { mutableStateOf("X") }
        val initialBoard = Array(3) { arrayOfNulls<String>(3) }
        val initialPlayer = "X"
        val elapsedTime = remember { mutableLongStateOf(0L) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                if (winner.value == null && board.value.any { it.any { it != null } }) {
                    elapsedTime.longValue++
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${elapsedTime.longValue / 60}:${(elapsedTime.longValue % 60).toString().padStart(2, '0')}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 100.dp)
            )
            Column {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            LargeFloatingActionButton(
                                onClick = {
                                    if (board.value[row][col] == null && winner.value == null) {
                                        val newBoard = board.value.map { it.clone() }.toTypedArray()
                                        newBoard[row][col] = currentPlayer.value
                                        board.value = newBoard
                                        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
                                        winner.value = checkForWinner(newBoard)
                                        if (winner.value != null || isBoardFull(newBoard)) {
                                            val result = winner.value ?: "D"
                                            val elapsedTimeToGiveToResultPage = elapsedTime.longValue
                                            onNavigateToTicTacToeResultPage(result, passwordStrength, elapsedTimeToGiveToResultPage, password)
                                        } else {
                                            makeBotMove(newBoard, currentPlayer, winner, passwordStrength)
                                            winner.value = checkForWinner(newBoard)
                                            if (winner.value != null || isBoardFull(newBoard)) {
                                                val result = winner.value ?: "D"
                                                val elapsedTimeToGiveToResultPage = elapsedTime.longValue
                                                onNavigateToTicTacToeResultPage(result, passwordStrength, elapsedTimeToGiveToResultPage, password)
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = board.value[row][col] ?: "",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        board.value = initialBoard
                        currentPlayer.value = initialPlayer
                        winner.value = null
                        elapsedTime.longValue = 0
                    },
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Zurücksetzen")
                }
            }
        }
    }
}

private fun checkForWinner(board: Array<Array<String?>>): String? {
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

private fun isBoardFull(board: Array<Array<String?>>): Boolean {
    for (row in board) {
        for (cell in row) {
            if (cell == null) {
                return false
            }
        }
    }
    return true
}

private fun makeBotMove(
    board: Array<Array<String?>>,
    currentPlayer: MutableState<String>,
    winner: MutableState<String?>,
    passwordStrength: Int
) {
    if (winner.value == null) {
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col] == null) {
                    availableMoves.add(row to col)
                }
            }
        }

        // Adjust minimax depth based on password strength
        val depth = when {
            passwordStrength <= 10 -> 1
            passwordStrength <= 20 -> 1
            passwordStrength <= 30 -> 2
            passwordStrength <= 40 -> 3
            passwordStrength <= 50 -> 4
            passwordStrength <= 60 -> 5
            passwordStrength <= 70 -> 5
            passwordStrength <= 80 -> 6
            passwordStrength <= 90 -> 7
            else -> 8
        }

        val bestMove = findBestMove(board, depth)
        if (bestMove != null) {
            board[bestMove.first][bestMove.second] = "O"
        }

        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
        winner.value = checkForWinner(board)
    }
}

private fun findBestMove(board: Array<Array<String?>>, depth: Int): Pair<Int, Int>? {
    var bestScore = Int.MIN_VALUE
    var bestMove: Pair<Int, Int>? = null

    for (row in board.indices) {
        for (col in board[row].indices) {
            if (board[row][col] == null) {
                val newBoard = board.map { it.copyOf() }.toTypedArray()
                newBoard[row][col] = "O"
                val score = minimax(newBoard, depth, false, 0)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = row to col
                }
            }
        }
    }

    return bestMove
}

private fun minimax(
    board: Array<Array<String?>>,
    depth: Int,
    isMaximizingPlayer: Boolean,
    currentDepth: Int
): Int {
    val result = checkForWinner(board)
    if (result != null) {
        return if (result == "O") 10 else -10
    }

    if (isBoardFull(board) || currentDepth == depth) {
        return 0
    }

    if (isMaximizingPlayer) {
        var bestScore = Int.MIN_VALUE
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col] == null) {
                    val newBoard = board.map { it.copyOf() }.toTypedArray()
                    newBoard[row][col] = "O"
                    val score = minimax(newBoard, depth, false, currentDepth + 1)
                    bestScore = max(score, bestScore)
                }
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col] == null) {
                    val newBoard = board.map { it.copyOf() }.toTypedArray()
                    newBoard[row][col] = "X"
                    val score = minimax(newBoard, depth, true, currentDepth + 1)
                    bestScore = min(score, bestScore)
                }
            }
        }
        return bestScore
    }
}