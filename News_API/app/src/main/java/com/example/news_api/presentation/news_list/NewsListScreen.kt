package com.example.news_api.presentation.news_list

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.news_api.BuildConfig
import com.example.news_api.R
import com.example.news_api.data.remote.RetrofitInstance
import com.example.news_api.data.remote.model.NewsDto

@Composable
fun NewsListScreen(navController: NavHostController) {
    // Define as categorias de notícias que podem ser selecionadas
    val categories = listOf("sports", "world", "technology", "movies")
    var selectedCategory by remember { mutableStateOf("sports") } // Categoria inicialmente selecionada
    var newsList by remember { mutableStateOf<List<NewsDto>>(emptyList()) } // Lista de notícias a ser mostrada
    var errorMessage by remember { mutableStateOf("") } // Mensagem de erro, caso ocorra

    // A cada mudança na categoria selecionada, busca as notícias dessa categoria
    LaunchedEffect(selectedCategory) {
        try {
            errorMessage = ""
            val response = RetrofitInstance.api.getTopStories(selectedCategory, BuildConfig.NYT_API_KEY)
            newsList = response.results // Atualiza a lista de notícias com o resultado da API
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar notícias: ${e.message}" // Caso ocorra algum erro na requisição
        }
    }

    // Container principal do ecrã
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Define o fundo do ecrã com a cor do tema
    ) {
        Column {

            Image(
                painter = painterResource(id = R.drawable.nyt_logo),
                contentDescription = "New York Times",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .size(width = 140.dp, height = 40.dp), // Ajusta o tamanho da imagem
                alignment = Alignment.Center
            )

            // Mostra as categorias das notícias
            Row(
                modifier = Modifier
                    .fillMaxWidth() // Preenche toda a largura da tela
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Espaçamento ao redor da linha
                horizontalArrangement = Arrangement.SpaceBetween // Espaço igual entre os itens
            ) {
                categories.forEach { category ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, // Centraliza o texto
                        modifier = Modifier
                            .padding(horizontal = 8.dp) // Adiciona espaçamento entre os itens
                            .clickable { selectedCategory = category } // Atualiza a categoria selecionada ao clicar
                    ) {
                        Text(
                            text = category.uppercase(),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        )
                        // Mostra uma linha preta abaixo da categoria selecionada
                        if (selectedCategory == category) {
                            Spacer(modifier = Modifier.height(4.dp)) // Espaçamento entre o texto e a linha
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(2.dp)
                                    .background(Color.Black) // Linha preta para indicar seleção
                            )
                        }
                    }
                }
            }

            // Se ocorrer um erro ou a lista de notícias estiver vazia
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error, // Cor de erro
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza a mensagem
                )
            } else if (newsList.isEmpty()) {
                Text(
                    text = "A carregar notícias...", // Mensagem enquanto as notícias estão a ser carregadas
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza a mensagem
                )
            } else {
                // Mostra a lista de notícias com LazyColumn
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = Modifier
                        .fillMaxSize() // Preenche o espaço restante
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Espaçamento das bordas
                ) {
                    items(newsList) { news ->
                        // Para cada notícia, chama o componente NewsItem
                        NewsItem(news = news) {
                            // Ao clicar na notícia, navega para o ecrã de detalhes passando os parâmetros
                            navController.navigate(
                                "newsDetail/${Uri.encode(news.title)}/${Uri.encode(news.abstract)}/${Uri.encode(news.multimedia?.get(0)?.url ?: "")}/${Uri.encode(news.url)}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(news: NewsDto, onClick: () -> Unit) {
    // Cada item de notícia é mostrado dentro de um Card
    Card(
        modifier = Modifier
            .fillMaxWidth() // O card ocupa toda a largura disponível
            .padding(vertical = 8.dp) // Espaçamento entre os cards
            .clickable { onClick() }, // Chama a função onClick quando o card é clicado
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp) // Eleva o card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Preenche toda a largura do card
                .padding(16.dp), // Padding dentro do card
            verticalAlignment = Alignment.CenterVertically // Alinha verticalmente os itens
        ) {
            // Mostra a imagem se ela estiver disponível
            if (!news.multimedia.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(news.multimedia[0].url), // Carrega a imagem da URL
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp) // Define o tamanho da imagem
                        .clip(MaterialTheme.shapes.small), // Aplica bordas arredondadas na imagem
                    contentScale = ContentScale.Crop // A imagem é cortada para se ajustar ao tamanho
                )
                Spacer(modifier = Modifier.width(16.dp)) // Espaçamento entre a imagem e o texto
            }
            // Mostra o título e o resumo da notícia
            Column {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium, // Estilo do título
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = news.abstract,
                    style = MaterialTheme.typography.bodySmall, // Estilo do resumo
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f) // Cor mais suave para o resumo
                )
            }
        }
    }
}
