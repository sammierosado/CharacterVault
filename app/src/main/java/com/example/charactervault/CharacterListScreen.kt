package com.example.charactervault

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.charactervault.model.Character
import com.example.charactervault.model.Image
import com.example.charactervault.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun CharacterListScreen(navController: NavController) {
    var characters by remember { mutableStateOf(listOf<Character>()) }
    var filteredCharacters by remember { mutableStateOf(listOf<Character>()) }
    var searchText by remember { mutableStateOf("") }
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
                filteredCharacters = characters
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
            TextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    filteredCharacters = if (newText.isEmpty()) {
                        characters
                    } else {
                        characters.filter { it.name.contains(newText, ignoreCase = true) }
                    }
                },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                    items(filteredCharacters) { character ->
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
            painter = rememberAsyncImagePainter(
                model = character.image.icon_url,
                imageLoader = ImageLoader.Builder(LocalContext.current)
                    .crossfade(true)
                    .build()
            ),
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
