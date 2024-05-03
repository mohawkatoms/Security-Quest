import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun TicTacToe(onNavigateToTicTacToeResultPage: (String, Int) -> Unit, passwordStrength: Int) {
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
                                            val newBoard = board.value.map { it.clone() }.toTypedArray() // Create a copy of the current board
                                            newBoard[row][col] = currentPlayer.value // Update the new board
                                            board.value = newBoard // Assign the new board to the state
                                            currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
                                            winner.value = checkForWinner(newBoard) // Check for winner using the new board
                                            if (winner.value != null || isBoardFull(newBoard)) {
                                                val result = if (winner.value != null) winner.value!! else "D"
                                                onNavigateToTicTacToeResultPage(result, passwordStrength)
                                            } else {
                                                makeBotMove(newBoard, currentPlayer, winner, passwordStrength)
                                                winner.value = checkForWinner(newBoard) // Check for winner after bot move
                                                if (winner.value != null || isBoardFull(newBoard)) {
                                                    val result = if (winner.value != null) winner.value!! else "D"
                                                    onNavigateToTicTacToeResultPage(result, passwordStrength)
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(6.dp),
                                    shape = CircleShape
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
                        },
                        modifier = Modifier.padding(top = 60.dp).align(Alignment.CenterHorizontally)

                    ) {
                        Text(text = "Zürücksetzen")
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

fun makeBotMove(board: Array<Array<String?>>, currentPlayer: MutableState<String>, winner: MutableState<String?>, passwordStrength: Int) {
    if (winner.value == null) {
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col] == null) {
                    availableMoves.add(row to col)
                }
            }
        }

        // Choose move based on bot strength
        when (passwordStrength) {
            in 1..19 -> randomMove(board, availableMoves)
            in 20..39 -> blockOrWinMove(board, currentPlayer.value, availableMoves)
            in 40..59 -> smartMove(board, currentPlayer.value, availableMoves)
            in 60..79 -> aggressiveMove(board, currentPlayer.value, availableMoves)
            in 80..89 -> defensiveMove(board, currentPlayer.value, availableMoves)
            100 -> unbeatableMove(board, availableMoves)
        }

        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
        winner.value = checkForWinner(board)
    }
}

fun randomMove(board: Array<Array<String?>>, availableMoves: MutableList<Pair<Int, Int>>) {
    val move = availableMoves.random()
    board[move.first][move.second] = "O"
}

fun blockOrWinMove(board: Array<Array<String?>>, value: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // Check rows for a potential win or block
    for (row in 0..2) {
        val rowValues = board[row]
        if (rowValues.count { it == value } == 2 && rowValues.count { it == null } == 1) {
            val colToFill = rowValues.indexOf(null)
            board[row][colToFill] = "O"
            return
        }
    }

    // Check columns for a potential win or block
    for (col in 0..2) {
        val colValues = arrayOf(board[0][col], board[1][col], board[2][col])
        if (colValues.count { it == value } == 2 && colValues.count { it == null } == 1) {
            val rowToFill = colValues.indexOf(null)
            board[rowToFill][col] = "O"
            return
        }
    }

    // Check diagonals for a potential win or block
    val diagonal1 = arrayOf(board[0][0], board[1][1], board[2][2])
    val diagonal2 = arrayOf(board[0][2], board[1][1], board[2][0])

    if ((diagonal1.count { it == value } == 2 && diagonal1.count { it == null } == 1)) {
        val index = diagonal1.indexOf(null)
        if (index != -1) {
            board[index][index] = "O"
            return
        }
    }

    if ((diagonal2.count { it == value } == 2 && diagonal2.count { it == null } == 1)) {
        val index = diagonal2.indexOf(null)
        if (index != -1) {
            board[index][2 - index] = "O"
            return
        }
    }

    // If no immediate win or block is possible, make a random move
    randomMove(board, availableMoves)
}

fun aggressiveMove(board: Array<Array<String?>>, value: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // If there's a winning move, take it
    blockOrWinMove(board, value, availableMoves)

    // If no winning move available, take a random move
    if (board.flatten().count { it == null } == 9) {
        randomMove(board, availableMoves)
    }
}

fun defensiveMove(board: Array<Array<String?>>, value: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // If there's a move to block the opponent, take it
    blockOrWinMove(board, if (value == "X") "O" else "X", availableMoves)

    // If no block available, take a random move
    if (board.flatten().count { it == null } == 9) {
        randomMove(board, availableMoves)
    }
}

fun smartMove(board: Array<Array<String?>>, value: String, availableMoves: MutableList<Pair<Int, Int>>) {
    // First, try to win
    blockOrWinMove(board, value, availableMoves)

    // If no winning move available, block opponent
    if (board.flatten().count { it == null } == 9) {
        blockOrWinMove(board, if (value == "X") "O" else "X", availableMoves)
    }

    // If still no moves made, make a random move
    if (board.flatten().count { it == null } == 9) {
        randomMove(board, availableMoves)
    }
}

fun unbeatableMove(board: Array<Array<String?>>, availableMoves: MutableList<Pair<Int, Int>>) {
    fun minimax(board: Array<Array<String?>>, depth: Int, isMaximizingPlayer: Boolean): Int {
        val result = checkForWinner(board)
        if (result != null) {
            return if (result == "O") 10 else -10
        }

        if (isBoardFull(board)) {
            return 0
        }

        if (isMaximizingPlayer) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j] == null) {
                        val newBoard = board.map { it.copyOf() }.toTypedArray()
                        newBoard[i][j] = "O"
                        val score = minimax(newBoard, depth + 1, false)
                        bestScore = maxOf(score, bestScore)
                    }
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j] == null) {
                        val newBoard = board.map { it.copyOf() }.toTypedArray()
                        newBoard[i][j] = "X"
                        val score = minimax(newBoard, depth + 1, true)
                        bestScore = minOf(score, bestScore)
                    }
                }
            }
            return bestScore
        }
    }

    var bestScore = Int.MIN_VALUE
    var bestMove = Pair(-1, -1)
    for (move in availableMoves) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[move.first][move.second] = "O"
        val score = minimax(newBoard, 0, false)
        if (score > bestScore) {
            bestScore = score
            bestMove = move
        }
    }
    board[bestMove.first][bestMove.second] = "O"
}
