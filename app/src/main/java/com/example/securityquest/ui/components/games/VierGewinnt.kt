import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun VierGewinnt(onNavigateToVierGewinntResultPage: (String, Int, Long) -> Unit, passwordStrength: Int) {
    Box (modifier = Modifier.padding(top = 160.dp)){
        val board = remember {
            mutableStateOf(Array(6) { arrayOfNulls<String>(7) })
        }
        val currentPlayer = remember {
            mutableStateOf("X")
        }
        val winner = remember {
            mutableStateOf<String?>(null)
        }
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
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 100.dp)
            )
            for (row in board.value.indices) {
                Row {
                    for (col in board.value[row].indices) {
                        FilledIconButton(
                            onClick = {
                                if (winner.value == null) {
                                    val rowIndex = findLowestEmptyRow(board.value, col)
                                    if (rowIndex != -1) {
                                        val newBoard = board.value.map { it.clone() }.toTypedArray()
                                        newBoard[rowIndex][col] = currentPlayer.value
                                        board.value = newBoard
                                        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
                                        winner.value = checkForWinners(newBoard)
                                        if (winner.value != null || isBoardFulls(newBoard)) {
                                            val result = if (winner.value != null) winner.value!! else "D"
                                            val elapsedTimeToGiveToResultPage = elapsedTime.longValue
                                            onNavigateToVierGewinntResultPage(result, passwordStrength, elapsedTimeToGiveToResultPage)
                                        } else {
                                            makeBotMoves(newBoard, currentPlayer, winner, passwordStrength)
                                            winner.value = checkForWinners(newBoard)
                                            if (winner.value != null || isBoardFulls(newBoard)) {
                                                val result = if (winner.value != null) winner.value!! else "D"
                                                val elapsedTimeToGiveToResultPage = elapsedTime.longValue
                                                onNavigateToVierGewinntResultPage(result, passwordStrength, elapsedTimeToGiveToResultPage)
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.padding(3.dp),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = board.value[row][col] ?: "",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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

fun makeBotMoves(board: Array<Array<String?>>, currentPlayer: MutableState<String>, winner: MutableState<String?>, passwordStrength: Int) {
    if (winner.value == null) {
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        for (col in board[0].indices) {
            val rowIndex = findLowestEmptyRow(board, col)
            if (rowIndex != -1) {
                availableMoves.add(rowIndex to col)
            }
        }

        // Choose move based on bot strength
        when (passwordStrength) {
            in 1..19 -> randomMoves(board, availableMoves)
            in 20..39 -> smartMoves(board, currentPlayer.value, availableMoves)
            in 40..59 -> blockOrWinMoves(board, currentPlayer.value, availableMoves)
            in 60..79 -> aggressiveMoves(board, currentPlayer.value, availableMoves)
            in 80..89 -> defensiveMoves(board, currentPlayer.value, availableMoves)
            100 -> unbeatableMoves(board, currentPlayer.value)
        }

        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
        winner.value = checkForWinner(board)
    }
}

fun randomMoves(board: Array<Array<String?>>, availableMoves: MutableList<Pair<Int, Int>>) {
    val move = availableMoves.random()
    board[move.first][move.second] = "O"
}

fun smartMoves(board: Array<Array<String?>>, currentPlayer: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // Implementing a basic AI strategy
    val centerCol = board[0].size / 2
    for ((row, col) in availableMoves) {
        if (col == centerCol) {
            if (shouldSwitchToNextColumn(board, row, col, currentPlayer)) {
                continue // Skip this move and move to the next one
            }
            board[row][col] = "O"
            return
        }
    }
    // If center column is not available or the condition to switch to the next column is met, choose randomly
    randomMoves(board, availableMoves)
}

private fun shouldSwitchToNextColumn(board: Array<Array<String?>>, row: Int, col: Int, currentPlayer: String): Boolean {
    if (row >= board.size - 3) {
        return false // Not enough space at the top of the column
    }
    // Check the top 3 rows of the column
    for (i in row until row + 3) {
        if (board[i][col] != currentPlayer) {
            return false // Last symbol in this column was not placed by the player
        }
    }
    // All conditions are met to switch to the next column
    return true
}
fun blockOrWinMoves(board: Array<Array<String?>>, currentPlayer: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // Check if there's a move that can make the current player win
    for ((row, col) in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = currentPlayer
        if (checkForWinners(newBoard) == currentPlayer) {
            board[row][col] = "O"
            return
        }
    }

    // Check if there's a move that can block the opponent from winning
    val opponent = if (currentPlayer == "X") "O" else "X"
    for ((row, col) in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = opponent
        if (checkForWinners(newBoard) == opponent) {
            board[row][col] = "O"
            return
        }
    }

    // If no winning move or blocking move, choose randomly
    randomMoves(board, availableMoves)
}

fun aggressiveMoves(board: Array<Array<String?>>, currentPlayer: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // Check if there's a move that can make the current player win
    for ((row, col) in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = currentPlayer
        if (checkForWinners(newBoard) == currentPlayer) {
            board[row][col] = "O"
            return
        }
    }

    // Check if there's a move that can block the opponent from winning
    val opponent = if (currentPlayer == "X") "O" else "X"
    for ((row, col) in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = opponent
        if (checkForWinners(newBoard) == opponent) {
            board[row][col] = "O"
            return
        }
    }

    // If no winning or blocking move, choose randomly
    randomMoves(board, availableMoves)
}

fun defensiveMoves(board: Array<Array<String?>>, currentPlayer: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // Check if there's a move that can block the opponent from winning
    val opponent = if (currentPlayer == "X") "O" else "X"
    for ((row, col) in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = opponent
        if (checkForWinners(newBoard) == opponent) {
            board[row][col] = "O"
            return
        }
    }

    // Check if there's a move that can make the current player win
    for ((row, col) in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = currentPlayer
        if (checkForWinners(newBoard) == currentPlayer) {
            board[row][col] = "O"
            return
        }
    }

    // If no winning or blocking move, choose randomly
    randomMoves(board, availableMoves)
}

fun unbeatableMoves(board: Array<Array<String?>>, currentPlayer: String) {
    val depth = 5 // Depth of search for the minimax algorithm
    val bestMove = minimax(board, currentPlayer, depth, Int.MIN_VALUE, Int.MAX_VALUE, true).second
    board[bestMove.first][bestMove.second] = "O"
}

fun minimax(
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

    val result = checkForWinners(board)
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

fun evaluate(board: Array<Array<String?>>, currentPlayer: String): Int {
    val opponent = if (currentPlayer == "X") "O" else "X"
    val playerScore = evaluatePlayer(board, currentPlayer)
    val opponentScore = evaluatePlayer(board, opponent)
    return playerScore - opponentScore
}

fun evaluatePlayer(board: Array<Array<String?>>, player: String): Int {
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
private fun checkForWinners(board: Array<Array<String?>>): String? {
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