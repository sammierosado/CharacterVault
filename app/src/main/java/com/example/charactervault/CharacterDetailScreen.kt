package com.example.charactervault

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.charactervault.model.CharacterDetail
import com.example.charactervault.network.RetrofitInstance
import com.example.charactervault.network.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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
                            apiKey = BuildConfig.GIANT_BOMB_API_KEY
                        )
                    }
                    Log.d("CharacterDetail", "Character Details: $response")
                    characterDetail = response.results
                } catch (e: Exception) {
                    errorMessage = "Failed to load character details: ${e.message}"
                    e.printStackTrace()
                    snackbarHostState.showSnackbar(message = errorMessage ?: "Unknown error")
                }
            } else {
                errorMessage = "No internet connection"
                snackbarHostState.showSnackbar(message = errorMessage ?: "No internet connection")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                characterDetail?.let { detail ->
                    Image(
                        painter = rememberAsyncImagePainter(detail.image?.medium_url),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = detail.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    detail.real_name?.let {
                        Text(text = "Real Name: $it", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    detail.aliases?.let {
                        Text(text = "Aliases: $it", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    detail.gender?.let {
                        val genderText = when (it) {
                            "1" -> "Male"
                            "2" -> "Female"
                            else -> "Other"
                        }
                        Text(text = "Gender: $genderText", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    detail.birthday?.let {
                        Text(text = "Birthday: $it", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    detail.first_appeared_in_game?.let {
                        Text(text = "First Appeared In: ${it.name}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(text = "Brief Summary (Deck)", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = detail.deck ?: "No brief description available",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Detailed Description", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    val parsedHtmlContent = parseHtmlContent(detail.description ?: "No detailed description available")
                    parsedHtmlContent.forEach { element ->
                        when (element) {
                            is HtmlText -> Text(text = element.text, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Justify)
                            is HtmlImage -> {
                                Image(
                                    painter = rememberAsyncImagePainter(element.url),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    detail.site_detail_url?.let {
                        Text(
                            text = "More Info: $it",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } ?: run {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

sealed class HtmlElement
data class HtmlText(val text: String) : HtmlElement()
data class HtmlImage(val url: String) : HtmlElement()

fun parseHtmlContent(html: String): List<HtmlElement> {
    val document: Document = Jsoup.parse(html)
    val elements = mutableListOf<HtmlElement>()

    document.body().allElements.forEach { element ->
        when (element.tagName()) {
            "p" -> elements.add(HtmlText(element.text()))
            "img" -> {
                val imgUrl = element.attr("src")
                if (imgUrl.isNotEmpty()) {
                    elements.add(HtmlImage(imgUrl))
                }
            }
            else -> {
                if (element.hasText()) {
                    elements.add(HtmlText(element.text()))
                }
            }
        }
    }

    return elements
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailScreenPreview() {
    CharacterDetailScreen(
        characterId = 1
    )
}