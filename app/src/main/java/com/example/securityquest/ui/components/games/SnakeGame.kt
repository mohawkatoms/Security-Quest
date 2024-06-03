package com.example.securityquest.ui.components.games

import kotlin.random.Random
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import kotlinx.coroutines.delay

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class Position(val x: Int, val y: Int)

@Composable
fun SnakeGame(onNavigateToSnakeResultPage: (Int, Int, Long, String, Int) -> Unit, passwordStrength: Int, password: String) {
    val level = (passwordStrength - 1) / 10 + 1
    Box(modifier = Modifier.padding(top = 130.dp)) {
        var snake by remember { mutableStateOf(listOf(Position(5, 5))) }
        var direction by remember { mutableStateOf(Direction.RIGHT) }
        var food by remember { mutableStateOf(generateFood(snake, level)) }
        var obstacles by remember { mutableStateOf(generateObstacles(level, snake)) }
        var score by remember { mutableIntStateOf(0) }
        var elapsedTime by remember { mutableLongStateOf(0L) }
        var isGameOver by remember { mutableStateOf(false) }
        var finalScore by remember {
            mutableIntStateOf(0)
        }

        // Calculate the points needed to win based on passwordStrength
        val pointsToWin = 3 + (27 * (passwordStrength - 1) / 99)

        LaunchedEffect(Unit) {
            while (!isGameOver) {
                delay(200)
                moveSnake(
                    snake = snake,
                    direction = direction,
                    food = food,
                    obstacles = obstacles,
                    onGameOver = { isGameOver = true },
                    onFoodEaten = {
                        score++
                        if (score == pointsToWin) {
                            finalScore = 1 // User wins
                            isGameOver = true
                        } else {
                            food = generateFood(snake, level)
                        }
                    }
                ).also { newSnake ->
                    snake = newSnake
                }
            }
        }

        LaunchedEffect(Unit) {
            while (!isGameOver) {
                delay(1000)
                elapsedTime++
            }
        }

        if (isGameOver) {
            onNavigateToSnakeResultPage(finalScore, passwordStrength, elapsedTime, password, score)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$score / $pointsToWin",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "${elapsedTime / 60}:${(elapsedTime % 60).toString().padStart(2, '0')}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 70.dp)
            )
            GameBoard(snake, food, obstacles)
            ControlButtons(onDirectionChange = { newDirection ->
                if (newDirection != direction.opposite()) {
                    direction = newDirection
                }
            })
        }
    }
}

@Composable
fun GameBoard(snake: List<Position>, food: Position, obstacles: List<Position>) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(1.dp)
    ) {
        snake.forEach { segment ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .offset(segment.x * 10.dp, segment.y * 10.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .offset(food.x * 10.dp, food.y * 10.dp)
                .background(MaterialTheme.colorScheme.error)
        )
        obstacles.forEach { obstacle ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .offset(obstacle.x * 10.dp, obstacle.y * 10.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Composable
fun ControlButtons(onDirectionChange: (Direction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloatingActionButton(onClick = { onDirectionChange(Direction.UP) }, Modifier.size(65.dp)) {
            Icon(Icons.Rounded.ArrowUpward, contentDescription = "Up")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(onClick = { onDirectionChange(Direction.LEFT) }, Modifier.size(65.dp)) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Left")
            }
            Spacer(modifier = Modifier.width(55.dp))
            FloatingActionButton(onClick = { onDirectionChange(Direction.RIGHT) }, Modifier.size(65.dp)) {
                Icon(Icons.Rounded.ArrowForward, contentDescription = "Right")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FloatingActionButton(onClick = { onDirectionChange(Direction.DOWN) }, Modifier.size(65.dp)) {
            Icon(Icons.Rounded.ArrowDownward, contentDescription = "Down")
        }
    }
}

private fun moveSnake(
    snake: List<Position>,
    direction: Direction,
    food: Position,
    obstacles: List<Position>,
    onGameOver: () -> Unit,
    onFoodEaten: () -> Unit
): List<Position> {
    val head = snake.first()
    val newHead = when (direction) {
        Direction.UP -> Position(head.x, head.y - 1)
        Direction.DOWN -> Position(head.x, head.y + 1)
        Direction.LEFT -> Position(head.x - 1, head.y)
        Direction.RIGHT -> Position(head.x + 1, head.y)
    }

    if (newHead in snake || newHead in obstacles || newHead.x !in 0 until 30 || newHead.y !in 0 until 30) {
        onGameOver()
        return snake
    }

    val newSnake = mutableListOf(newHead)
    newSnake.addAll(snake.take(if (newHead == food) snake.size else snake.size - 1))

    if (newHead == food) {
        onFoodEaten()
    }

    return newSnake
}

private fun generateFood(snake: List<Position>, level: Int): Position {
    var newFood: Position
    val wallDistance = if (level <= 6) 3 else 1
    do {
        newFood = Position(
            x = Random.nextInt(wallDistance, 30 - wallDistance),
            y = Random.nextInt(wallDistance, 30 - wallDistance)
        )
    } while (newFood in snake)
    return newFood
}

private fun generateObstacles(level: Int, snake: List<Position>): List<Position> {
    val obstacleCount = (level - 1) * 1.2 + 3
    val obstacles = mutableListOf<Position>()
    while (obstacles.size < obstacleCount) {
        val obstacle = Position(Random.nextInt(30), Random.nextInt(30))
        if (obstacle !in snake && obstacle !in obstacles && obstacle != Position(5, 5) && obstacle !in snake.startingPath()) {
            obstacles.add(obstacle)
        }
    }
    return obstacles
}

private fun Direction.opposite(): Direction {
    return when (this) {
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
        Direction.LEFT -> Direction.RIGHT
        Direction.RIGHT -> Direction.LEFT
    }
}

private fun List<Position>.startingPath(): List<Position> {
    val path = mutableListOf<Position>()
    for (i in 1 until 5) {
        path.add(Position(5 + i, 5))
    }
    return path
}
