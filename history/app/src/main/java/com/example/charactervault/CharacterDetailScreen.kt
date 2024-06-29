package com.example.charactervault

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.charactervault.model.CharacterDetail
import com.example.charactervault.network.RetrofitInstance
import com.example.charactervault.network.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CharacterDetailScreen(characterId: Int) {
    val context = LocalContext.current
    var characterDetail by remember { mutableStateOf<CharacterDetail?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(characterId) {
        coroutineScope.launch {
            if (isNetworkAvailable(context)) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.getCharacterDetails(
                            id = characterId,
                            apiKey = "ea98adc584efb356fcd14b949f8a9f2aa2b270b8"
                        )
                    }
                    characterDetail = response.results
                } catch (e: Exception) {
                    errorMessage = "Failed to load character details: ${e.message}"
                    e.printStackTrace()
                    snackbarHostState.showSnackbar(message = errorMessage ?: "Unknown error")
                }
            } else {
                errorMessage = "No internet connection"
                snackbarHostState.showSnackbar(message = errorMessage!!)
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
            characterDetail?.let { detail ->
                Image(
                    painter = rememberAsyncImagePainter(detail.image.medium_url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = detail.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = detail.real_name ?: "", style = MaterialTheme.typography.bodyLarge)
                Text(text = detail.aliases ?: "", style = MaterialTheme.typography.bodyLarge)
                Text(text = detail.deck ?: "", style = MaterialTheme.typography.bodyLarge)
                Text(text = detail.description ?: "", style = MaterialTheme.typography.bodyLarge)
            } ?: run {
                Text(text = "Loading...", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailScreenPreview() {
    CharacterDetailScreen(
        characterId = 1
    )
}
