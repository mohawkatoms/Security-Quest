package com.example.securityquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.securityquest.ui.page.LeaderboardPage
import com.example.securityquest.ui.page.SnakePage
import com.example.securityquest.ui.page.StartingPage
import com.example.securityquest.ui.page.TicTacToePage
import com.example.securityquest.ui.page.VierGewinntPage
import com.example.securityquest.ui.page.resultpage.SnakeResultPage
import com.example.securityquest.ui.page.resultpage.TicTacToeResultPage
import com.example.securityquest.ui.page.resultpage.VierGewinntResultPage
import com.example.securityquest.ui.theme.SecurityQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            SecurityQuestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Navigation
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "startingPage") {
                        composable("startingPage") { StartingPage(onNavigateToTicTacToePage = {passwordStrength, password -> navController.navigate("ticTacToePage/$passwordStrength/$password" )}, onNavigateToVierGewinntPage = {passwordStrength, password -> navController.navigate("vierGewinntPage/$passwordStrength/$password")}, onNavigateToSnakePage = {passwordStrength, password -> navController.navigate("snakePage/$passwordStrength/$password")}, onNavigateToLeaderboardPage = {navController.navigate("leaderboardPage")}, password = "") }
                        //Starting Page with Password coming from the leaderbord
                        composable("startingPage/{password}", arguments = listOf(
                            navArgument("password") { type = NavType.StringType }
                        )) { entry ->
                            val password = entry.arguments?.getString("password") ?: ""
                            StartingPage(password = password, onNavigateToTicTacToePage = { passwordStrength, userPassword ->
                                navController.navigate("ticTacToePage/$passwordStrength/$userPassword")
                            }, onNavigateToVierGewinntPage = { passwordStrength, userPassword ->
                                navController.navigate("vierGewinntPage/$passwordStrength/$userPassword")
                            }, onNavigateToSnakePage = { passwordStrength, userPassword ->
                                navController.navigate("snakePage/$passwordStrength/$userPassword")
                            }, onNavigateToLeaderboardPage = {
                                navController.navigate("leaderboardPage")
                            })
                        }
                        //Tic Tac Toe
                        composable("ticTacToePage/{passwordStrength}/{password}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType},
                            navArgument("password") {type = NavType.StringType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            val password = it.arguments?.getString("password") ?: ""
                            TicTacToePage(navController = navController, passwordStrength = passwordStrength, password = password, onNavigateToTicTacToeResultPage = {status, strength, time, userPassword -> navController.navigate("ticTacToeResultPage/$status/$strength/$time/$userPassword")})}
                        //Tic Tac Toe Result Page
                        composable("ticTacToeResultPage/{status}/{strength}/{time}/{password}", arguments = listOf(
                            navArgument("status") {type = NavType.StringType},
                            navArgument("strength") {type = NavType.IntType},
                            navArgument("time") {type = NavType.LongType},
                            navArgument("password") {type = NavType.StringType}
                        )) { val status = it.arguments?.getString("status") ?: ""
                            val strength = it.arguments?.getInt("strength") ?: 1
                            val time = it.arguments?.getLong("time") ?: 1L
                            val password = it.arguments?.getString("password") ?: ""
                            TicTacToeResultPage(status = status, navController = navController, passwordStrength = strength, time = time, password = password)
                        }

                        //Vier Gewinnt
                        composable("vierGewinntPage/{passwordStrength}/{password}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType},
                            navArgument("password") {type = NavType.StringType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            val password = it.arguments?.getString("password") ?: ""
                            VierGewinntPage(navController = navController, passwordStrength = passwordStrength, password = password, onNavigateToVierGewinntResultPage = {status, strength, time, userPassword -> navController.navigate("vierGewinntResultPage/$status/$strength/$time/$userPassword")})}
                        //Vier Gewinnt Result Page
                        composable("vierGewinntResultPage/{status}/{strength}/{time}/{password}", arguments = listOf(
                            navArgument("status") {type = NavType.StringType},
                            navArgument("strength") {type = NavType.IntType},
                            navArgument("time") {type = NavType.LongType},
                            navArgument("password") {type = NavType.StringType}
                        )) { val status = it.arguments?.getString("status") ?: ""
                            val strength = it.arguments?.getInt("strength") ?: 1
                            val time = it.arguments?.getLong("time") ?: 1L
                            val password = it.arguments?.getString("password") ?: ""
                            VierGewinntResultPage(status = status, navController = navController, passwordStrength = strength, time = time, password = password )
                        }

                        //Snake
                        composable("SnakePage/{passwordStrength}/{password}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType},
                            navArgument("password") {type = NavType.StringType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            val password = it.arguments?.getString("password") ?: ""
                            SnakePage(navController = navController, passwordStrength = passwordStrength, password = password, onNavigateToSnakeResultPage = {score, strength, time, userPassword, points -> navController.navigate("snakeResultPage/$score/$strength/$time/$userPassword/$points")})}
                        //Snake Result Page
                        composable("snakeResultPage/{score}/{strength}/{time}/{password}/{points}", arguments = listOf(
                            navArgument("score") {type = NavType.IntType},
                            navArgument("strength") {type = NavType.IntType},
                            navArgument("time") {type = NavType.LongType},
                            navArgument("password") {type = NavType.StringType},
                            navArgument("points") {type = NavType.IntType}
                        )) { val score = it.arguments?.getInt("score") ?: 1
                            val strength = it.arguments?.getInt("strength") ?: 1
                            val time = it.arguments?.getLong("time") ?: 1L
                            val password = it.arguments?.getString("password") ?: ""
                            val points = it.arguments?.getInt("points") ?: 1
                            SnakeResultPage(score = score, navController = navController, passwordStrength = strength, time = time, password = password, points = points )
                        }
                        //Leaderboard
                        composable("leaderboardPage"){
                            LeaderboardPage(navController)
                        }
                    }
                }
            }
        }
    }
}



