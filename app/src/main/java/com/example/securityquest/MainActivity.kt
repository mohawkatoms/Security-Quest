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
import com.example.securityquest.ui.page.BrickPage
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
                        composable("startingPage") { StartingPage(onNavigateToTicTacToePage = {navController.navigate("ticTacToePage/$it" )}, onNavigateToVierGewinntPage = {navController.navigate("vierGewinntPage/$it")}, onNavigateToBrickPage = {navController.navigate("brickPage/$it")}) }
                        //Tic Tac Toe
                        composable("ticTacToePage/{passwordStrength}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            TicTacToePage(navController = navController, passwordStrength = passwordStrength, onNavigateToTicTacToeResultPage = {status, strength -> navController.navigate("ticTacToeResultPage/$status/$strength")})}
                        //Tic Tac Toe Result Page
                        composable("ticTacToeResultPage/{status}/{strength}", arguments = listOf(
                            navArgument("status") {type = NavType.StringType},
                            navArgument("strength") {type = NavType.IntType}
                        )) { val status = it.arguments?.getString("status") ?: ""
                            val strength = it.arguments?.getInt("strength") ?: 1
                            TicTacToeResultPage(status = status, navController = navController, passwordStrength = strength )
                        }

                        //Vier Gewinnt
                        composable("vierGewinntPage/{passwordStrength}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            VierGewinntPage(navController = navController, passwordStrength = passwordStrength, onNavigateToVierGewinntResultPage = {status, strength -> navController.navigate("vierGewinntResultPage/$status/$strength")})}
                        //Vier Gewinnt Result Page
                        composable("vierGewinntResultPage/{status}/{strength}", arguments = listOf(
                            navArgument("status") {type = NavType.StringType},
                            navArgument("strength") {type = NavType.IntType}
                        )) { val status = it.arguments?.getString("status") ?: ""
                            val strength = it.arguments?.getInt("strength") ?: 1
                            VierGewinntResultPage(status = status, navController = navController, passwordStrength = strength )
                        }

                        //Brick
                        composable("brickPage/{passwordStrength}", arguments = listOf(
                            navArgument("passwordStrength") {type = NavType.IntType}
                        )) { val passwordStrength = it.arguments?.getInt("passwordStrength") ?: 1
                            BrickPage(navController = navController, passwordStrength = passwordStrength)}
                    }
                }
            }
        }
    }
}



