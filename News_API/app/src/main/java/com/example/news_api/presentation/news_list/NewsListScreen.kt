package com.example.news_api.presentation.news_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.news_api.BuildConfig
import com.example.news_api.R
import com.example.news_api.data.remote.RetrofitInstance
import com.example.news_api.data.remote.model.NewsDto

/**
 * Função composable que apresenta a lista de notícias no ecrã principal da aplicação.
 *
 * Esta função carrega as notícias de uma API remota, mostra mensagens de erro ou estado de carregamento,
 * e apresenta cada notícia numa lista clicável. O utilizador pode navegar para os detalhes de cada notícia.
 *
 * @param navController Controlador de navegação para permitir transições entre ecrãs.
 */
@Composable
fun NewsListScreen(navController: NavHostController) {
    // Lista de notícias obtida da API
    var newsList by remember { mutableStateOf<List<NewsDto>>(emptyList()) }
    // Mensagem de erro em caso de falha
    var errorMessage by remember { mutableStateOf("") }

    // Carrega as notícias ao iniciar o composable
    LaunchedEffect(Unit) {
        try {
            // Obtém as notícias da API
            val response = RetrofitInstance.api.getTopStories("sports", BuildConfig.NYT_API_KEY)
            newsList = response.results
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar notícias: ${e.message}"
        }
    }

    // Layout principal da tela
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mostra uma mensagem de erro, caso exista
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Mostra uma mensagem enquanto as notícias estão a ser carregadas
        else if (newsList.isEmpty()) {
            Text(
                text = "A carregar notícias...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Mostra a lista de notícias
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Cabeçalho com o logótipo do New York Times
                item {
                    Image(
                        painter = painterResource(id = R.drawable.nyt_logo), // Logótipo do NYT
                        contentDescription = "Logo do New York Times",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .size(width = 175.dp, height = 75.dp),
                        alignment = Alignment.Center
                    )
                }

                // Itens da lista de notícias
                items(newsList) { news ->
                    NewsItem(news = news) {
                        // Navega para o ecrã de detalhes ao clicar numa notícia
                        navController.navigate(
                            "newsDetail/${news.title}/${news.abstract}"
                        )
                    }
                }
            }
        }
    }
}

/**
 * Função composable que representa um único item de notícia na lista.
 *
 * Cada item inclui uma imagem (se disponível), o título da notícia e uma descrição curta.
 * Os itens são clicáveis, permitindo navegar para mais detalhes.
 *
 * @param news Objeto NewsDto que contém as informações da notícia.
 * @param onClick Ação a executar quando o item for clicado.
 */
@Composable
fun NewsItem(news: NewsDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Preenche toda a largura disponível
            .padding(vertical = 8.dp) // Espaçamento entre os cartões
            .clickable { onClick() }, // Torna o cartão clicável
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.White // Fundo branco do cartão
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 4.dp // Sombra para criar o efeito de flutuação
        )
    ) {
        // Layout em linha que mostra a imagem e os textos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Margem interna do cartão
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mostra a imagem da notícia, se disponível
            if (!news.multimedia.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(news.multimedia[0].url), // Carrega a imagem da URL
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp) // Tamanho da imagem
                        .clip(MaterialTheme.shapes.small), // Bordas arredondadas
                    contentScale = ContentScale.Crop // Ajusta a imagem para o tamanho especificado
                )
                Spacer(modifier = Modifier.width(16.dp)) // Espaçamento entre a imagem e o texto
            }
            // Coluna que mostra o título e o resumo da notícia
            Column {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium, // Estilo do título
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = news.abstract,
                    style = MaterialTheme.typography.bodySmall, // Estilo do resumo
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) // Texto com opacidade reduzida
                )
            }
        }
    }
}
