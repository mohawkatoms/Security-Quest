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
import com.example.securityquest.ui.page.SnakePage
import com.example.securityquest.ui.page.LeaderboardPage
import com.example.securityquest.ui.page.StartingPage
import com.example.securityquest.ui.page.TicTacToePage
import com.example.securityquest.ui.page.VierGewinntPage
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
                        composable("startingPage") { StartingPage(onNavigateToTicTacToePage = {navController.navigate("ticTacToePage/$it" )}, onNavigateToVierGewinntPage = {navController.navigate("vierGewinntPage/$it")}, onNavigateToBrickPage = {navController.navigate("brickPage/$it")}, onNavigateToLeaderboardPage = {navController.navigate("leaderboardPage")}) }
                        //Tic Tac Toe
                        composable("ticTacToePage/{passwordStrength}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            TicTacToePage(navController = navController, passwordStrength = passwordStrength, onNavigateToTicTacToeResultPage = {status, strength, time -> navController.navigate("ticTacToeResultPage/$status/$strength/$time")})}
                        //Tic Tac Toe Result Page
                        composable("ticTacToeResultPage/{status}/{strength}/{time}", arguments = listOf(
                            navArgument("status") {type = NavType.StringType},
                            navArgument("strength") {type = NavType.IntType},
                            navArgument("time") {type = NavType.LongType}
                        )) { val status = it.arguments?.getString("status") ?: ""
                            val strength = it.arguments?.getInt("strength") ?: 1
                            val time = it.arguments?.getLong("time") ?: 1L
                            TicTacToeResultPage(status = status, navController = navController, passwordStrength = strength, time = time )
                        }

                        //Vier Gewinnt
                        composable("vierGewinntPage/{passwordStrength}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            VierGewinntPage(navController = navController, passwordStrength = passwordStrength, onNavigateToVierGewinntResultPage = {status, strength, time -> navController.navigate("vierGewinntResultPage/$status/$strength/$time")})}
                        //Vier Gewinnt Result Page
                        composable("vierGewinntResultPage/{status}/{strength}/{time}", arguments = listOf(
                            navArgument("status") {type = NavType.StringType},
                            navArgument("strength") {type = NavType.IntType},
                            navArgument("time") {type = NavType.LongType}
                        )) { val status = it.arguments?.getString("status") ?: ""
                            val strength = it.arguments?.getInt("strength") ?: 1
                            val time = it.arguments?.getLong("time") ?: 1L
                            VierGewinntResultPage(status = status, navController = navController, passwordStrength = strength, time = time )
                        }

                        //Brick
                        composable("brickPage/{passwordStrength}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            SnakePage(navController = navController, passwordStrength = passwordStrength)}

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



