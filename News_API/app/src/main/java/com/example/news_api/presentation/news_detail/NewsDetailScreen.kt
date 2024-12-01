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
    // Variável para guardar o conteúdo do artigo
    var articleContent by remember { mutableStateOf("A carregar conteúdo...") }

    // Usado para chamar a função de carregar o conteúdo assim que o articleUrl for alterado
    LaunchedEffect(articleUrl) {
        // Carrega o conteúdo do artigo a partir do URL fornecido
        articleContent = fetchArticleBody(articleUrl)
    }

    // Componente principal, com scroll vertical
    Column(
        modifier = Modifier
            .fillMaxSize() // Preenche o ecrã
            .padding(16.dp) // Adiciona um padding de 16dp em volta de tudo
            .verticalScroll(rememberScrollState()) // Permite scroll vertical
    ) {
        // Mostra o título da notícia
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium, // Estilo de título
            color = Color.Black, // Cor preta para o título
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espaçamento de 16dp abaixo do título

        // Mostra o resumo da notícia
        Text(
            text = abstract,
            style = MaterialTheme.typography.bodyLarge, // Estilo de texto grande para o resumo
            color = MaterialTheme.colorScheme.onSurface // Cor do texto
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espaçamento de 16dp abaixo do resumo

        // Se a URL da imagem não for nula ou vazia, mostra a imagem
        if (!imageUrl.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl), // Carrega a imagem com o Coil
                contentDescription = "Imagem da notícia",
                modifier = Modifier
                    .fillMaxWidth() // A imagem ocupa toda a largura
                    .height(200.dp), // A altura da imagem fixa em 200dp
                contentScale = ContentScale.Crop // A imagem cortada para se ajustar ao tamanho
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento de 16dp abaixo da imagem
        }

        // Mostra o conteúdo completo do artigo
        Text(
            text = articleContent, // Conteúdo do artigo
            style = MaterialTheme.typography.bodyMedium, // Estilo do texto
            color = MaterialTheme.colorScheme.onSurface, // Cor do texto
            textAlign = TextAlign.Justify // Justifica o texto para melhor leitura
        )
    }
}

// Função para buscar o conteúdo do artigo a partir de um URL
suspend fun fetchArticleBody(url: String): String {
    return withContext(Dispatchers.IO) {
        try {
            // Faz uma requisição para obter o HTML da página
            val document = Jsoup.connect(url).get()

            // Procura por um script do tipo "application/ld+json" no HTML (geralmente contém os dados estruturados)
            val jsonScript = document.select("script[type=application/ld+json]").first()?.data()

            // Se o script for encontrado
            if (jsonScript != null) {
                // Converte o conteúdo do script para um objeto JSON
                val jsonObject = org.json.JSONObject(jsonScript)

                // Verifica se existe o campo "articleBody" no JSON
                if (jsonObject.has("articleBody")) {
                    // Retorna o conteúdo do corpo do artigo
                    return@withContext jsonObject.getString("articleBody")
                }
            }
            // Caso o campo "articleBody" não seja encontrado, retorna uma mensagem padrão
            "Conteúdo não encontrado."
        } catch (e: Exception) {
            // Em caso de erro, retorna uma mensagem
            "Erro ao carregar o conteúdo: ${e.message}"
        }
    }
}
