import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun VierGewinnt(onNavigateToVierGewinntResultPage: (String, Int, Long, String) -> Unit, passwordStrength: Int, password: String) {
    Box(modifier = Modifier.padding(top = 160.dp)) {
        val board = remember { mutableStateOf(Array(6) { arrayOfNulls<String>(7) }) }
        val currentPlayer = remember { mutableStateOf("X") }
        val winner = remember { mutableStateOf<String?>(null) }
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
                .padding(13.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${elapsedTime.longValue / 60}:${(elapsedTime.longValue % 60).toString().padStart(2, '0')}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 100.dp)
            )
            for (row in board.value.indices) {
                Row {
                    for (col in board.value[row].indices) {
                        val cellValue = board.value[row][col]
                        val backgroundColor = when (cellValue) {
                            "X" -> Color.Red
                            "O" -> Color.Yellow
                            else -> MaterialTheme.colorScheme.primary
                        }

                        val animatedOffsetY = animateFloatAsState(
                            targetValue = if (cellValue != null) 0f else 50f * (6 - row), label = ""
                        )

                        FilledIconButton(
                            onClick = {
                                if (winner.value == null) {
                                    val rowIndex = findLowestEmptyRow(board.value, col)
                                    if (rowIndex != -1) {
                                        val newBoard = board.value.map { it.clone() }.toTypedArray()
                                        newBoard[rowIndex][col] = currentPlayer.value
                                        board.value = newBoard
                                        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
                                        winner.value = checkForWinner(newBoard)
                                        if (winner.value != null || isBoardFulls(newBoard)) {
                                            val result = winner.value ?: "D"
                                            onNavigateToVierGewinntResultPage(result, passwordStrength, elapsedTime.longValue, password)
                                        } else {
                                            makeBotMove(newBoard, currentPlayer, winner, passwordStrength)
                                            winner.value = checkForWinner(newBoard)
                                            if (winner.value != null || isBoardFulls(newBoard)) {
                                                val result = winner.value ?: "D"
                                                onNavigateToVierGewinntResultPage(result, passwordStrength, elapsedTime.longValue, password)
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .offset(y = -animatedOffsetY.value.dp)
                                    .background(backgroundColor, CircleShape)
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    val initialBoard = Array(6) { arrayOfNulls<String>(7) }
                    board.value = initialBoard
                    currentPlayer.value = "X"
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

private fun makeBotMove(board: Array<Array<String?>>, currentPlayer: MutableState<String>, winner: MutableState<String?>, passwordStrength: Int) {
    if (winner.value == null) {
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        for (col in board[0].indices) {
            val rowIndex = findLowestEmptyRow(board, col)
            if (rowIndex != -1) {
                availableMoves.add(rowIndex to col)
            }
        }

        // Determine bot level based on password strength
        val depth = when (passwordStrength) {
            in 1..9 -> 1
            in 10..19 -> 2
            in 20..29 -> 2
            in 30..39 -> 3
            in 40..49 -> 3
            in 50..69 -> 4
            in 70..89 -> 5
            in 90..100 -> 6
            else -> 1
        }

        val bestMove = minimax(board, currentPlayer.value, depth, Int.MIN_VALUE, Int.MAX_VALUE, true).second
        if (bestMove.first != -1 && bestMove.second != -1) {
            board[bestMove.first][bestMove.second] = "O"
        }

        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
        winner.value = checkForWinner(board)
    }
}

private fun minimax(
    board: Array<Array<String?>>, currentPlayer: String,
    depth: Int, alpha: Int, beta: Int, maximizingPlayer: Boolean
): Pair<Int, Pair<Int, Int>> {
    val availableMoves = mutableListOf<Pair<Int, Int>>()
    for (col in board[0].indices) {
        val rowIndex = findLowestEmptyRow(board, col)
        if (rowIndex != -1) {
            availableMoves.add(rowIndex to col)
        }
    }

    val result = checkForWinner(board)
    if (depth == 0 || result != null) {
        return evaluate(board, currentPlayer) to Pair(-1, -1)
    }

    var alphaValue = alpha
    var betaValue = beta

    if (maximizingPlayer) {
        var value = Int.MIN_VALUE
        var bestMove = Pair(-1, -1)
        for (move in availableMoves) {
            val newBoard = board.map { it.copyOf() }.toTypedArray()
            newBoard[move.first][move.second] = currentPlayer
            val score = minimax(newBoard, currentPlayer, depth - 1, alphaValue, betaValue, false).first
            if (score > value) {
                value = score
                bestMove = move
            }
            alphaValue = maxOf(alphaValue, value)
            if (alphaValue >= betaValue) {
                break
            }
        }
        return value to bestMove
    } else {
        var value = Int.MAX_VALUE
        var bestMove = Pair(-1, -1)
        val opponent = if (currentPlayer == "X") "O" else "X"
        for (move in availableMoves) {
            val newBoard = board.map { it.copyOf() }.toTypedArray()
            newBoard[move.first][move.second] = opponent
            val score = minimax(newBoard, currentPlayer, depth - 1, alphaValue, betaValue, true).first
            if (score < value) {
                value = score
                bestMove = move
            }
            betaValue = minOf(betaValue, value)
            if (betaValue <= alphaValue) {
                break
            }
        }
        return value to bestMove
    }
}

private fun evaluate(board: Array<Array<String?>>, currentPlayer: String): Int {
    val opponent = if (currentPlayer == "X") "O" else "X"
    return evaluatePlayer(board, currentPlayer) - evaluatePlayer(board, opponent)
}

private fun evaluatePlayer(board: Array<Array<String?>>, player: String): Int {
    var score = 0
    for (row in board.indices) {
        for (col in board[row].indices) {
            if (board[row][col] == player) {
                // Check horizontal
                if (col <= board[0].size - 4) {
                    if (board[row][col + 1] == player && board[row][col + 2] == player && board[row][col + 3] == player) {
                        score += 100
                    }
                }
                // Check vertical
                if (row <= board.size - 4) {
                    if (board[row + 1][col] == player && board[row + 2][col] == player && board[row + 3][col] == player) {
                        score += 100
                    }
                }
                // Check diagonal \
                if (row <= board.size - 4 && col <= board[0].size - 4) {
                    if (board[row + 1][col + 1] == player && board[row + 2][col + 2] == player && board[row + 3][col + 3] == player) {
                        score += 100
                    }
                }
                // Check diagonal /
                if (row <= board.size - 4 && col >= 3) {
                    if (board[row + 1][col - 1] == player && board[row + 2][col - 2] == player && board[row + 3][col - 3] == player) {
                        score += 100
                    }
                }
            }
        }
    }
    return score
}

private fun findLowestEmptyRow(board: Array<Array<String?>>, columnIndex: Int): Int {
    for (rowIndex in board.indices.reversed()) {
        if (board[rowIndex][columnIndex] == null) {
            return rowIndex
        }
    }
    return -1 // Column is full
}

private fun checkForWinner(board: Array<Array<String?>>): String? {
    for (row in board.indices) {
        for (col in board[row].indices) {
            val player = board[row][col]
            if (player != null) { // Check if cell is not empty
                // Check horizontal
                if (checkLine(board, row, col, 1, 0, player)) return player

                // Check vertical
                if (checkLine(board, row, col, 0, 1, player)) return player

                // Check diagonal
                if (checkLine(board, row, col, 1, 1, player)) return player

                // Check anti-diagonal
                if (checkLine(board, row, col, 1, -1, player)) return player
            }
        }
    }
    return null
}

private fun checkLine(
    board: Array<Array<String?>>, row: Int, col: Int,
    rowDir: Int, colDir: Int, player: String): Boolean {
    var count = 1
    for (i in 1 until 4) {
        val newRow = row + i * rowDir
        val newCol = col + i * colDir
        if (newRow < 0 || newRow >= board.size || newCol < 0 || newCol >= board[0].size || board[newRow][newCol] != player) {
            break
        }
        count++
    }
    return count == 4
}

private fun isBoardFulls(board: Array<Array<String?>>): Boolean {
    for (row in board) {
        for (cell in row) {
            if (cell == null) return false
        }
    }
    return true
}