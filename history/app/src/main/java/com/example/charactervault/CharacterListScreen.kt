package com.example.charactervault.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.charactervault.model.Character
import com.example.charactervault.model.Image
import com.example.charactervault.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun CharacterListScreen(navController: NavController) {
    var characters by remember { mutableStateOf(listOf<Character>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters(
                    apiKey = "ea98adc584efb356fcd14b949f8a9f2aa2b270b8"
                )
                characters = response.results
            } catch (e: Exception) {
                errorMessage = "Failed to load characters: ${e.message}"
                e.printStackTrace()
                snackbarHostState.showSnackbar(message = errorMessage ?: "Unknown error")
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Character List",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(characters) { character ->
                        CharacterRow(character = character) {
                            navController.navigate("characterDetail/${character.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterRow(character: Character, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(character.image.icon_url),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = character.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterRowPreview() {
    CharacterRow(
        character = Character(
            id = 1,
            name = "Mario",
            image = Image(
                icon_url = "https://example.com/mario_icon.jpg",
                medium_url = "https://example.com/mario_medium.jpg"
            ),
            deck = "A plumber who often saves the Mushroom Kingdom."
        )
    ) {}
}
