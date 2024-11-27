package com.example.news_api.presentation.news_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@Composable
fun NewsDetailScreen(title: String, abstract: String, imageUrl: String?, articleUrl: String) {
    var articleContent by remember { mutableStateOf("A carregar conteúdo...") }

    // Realiza a chamada para obter o texto HTML
    LaunchedEffect(articleUrl) {
        articleContent = fetchArticleBody(articleUrl)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = abstract,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (!imageUrl.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Imagem da notícia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = articleContent,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Justify
        )
    }
}

suspend fun fetchArticleBody(url: String): String {
    return withContext(Dispatchers.IO) {
        try {
            // Faz a requisição para obter o HTML da página
            val document = Jsoup.connect(url).get()

            // Seleciona o script que tem o JSON-LD
            val jsonScript = document.select("script[type=application/ld+json]").first()?.data()

            if (jsonScript != null) {
                // Converte o conteúdo JSON para um objeto
                val jsonObject = org.json.JSONObject(jsonScript)

                // Extrai o campo "articleBody"
                if (jsonObject.has("articleBody")) {
                    return@withContext jsonObject.getString("articleBody")
                }
            }
            "Conteúdo não encontrado."
        } catch (e: Exception) {
            "Erro ao carregar o conteúdo: ${e.message}"
        }
    }
}

