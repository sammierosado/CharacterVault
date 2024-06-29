package com.example.charactervault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.charactervault.ui.CharacterListScreen
import com.example.charactervault.ui.theme.CharacterVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CharacterVaultTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "characterList") {
                        composable("characterList") {
                            CharacterListScreen(navController = navController)
                        }
                        composable(
                            "characterDetail/{characterId}",
                            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            CharacterDetailScreen(
                                characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CharacterVaultTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "characterList") {
            composable("characterList") {
                CharacterListScreen(navController = navController)
            }
            composable(
                "characterDetail/{characterId}",
                arguments = listOf(navArgument("characterId") { type = NavType.IntType })
            ) { backStackEntry ->
                CharacterDetailScreen(
                    characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
                )
            }
        }
    }
}
