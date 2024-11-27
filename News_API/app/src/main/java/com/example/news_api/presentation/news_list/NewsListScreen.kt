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
    val categories = listOf("sports", "world", "technology", "movies")
    var selectedCategory by remember { mutableStateOf("sports") }
    var newsList by remember { mutableStateOf<List<NewsDto>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(selectedCategory) {
        try {
            errorMessage = ""
            val response = RetrofitInstance.api.getTopStories(selectedCategory, BuildConfig.NYT_API_KEY)
            newsList = response.results
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar notícias: ${e.message}"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            // Logo do New York Times
            Image(
                painter = painterResource(id = R.drawable.nyt_logo),
                contentDescription = "Logo do New York Times",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .size(width = 140.dp, height = 40.dp),
                alignment = Alignment.Center
            )

            // Categorias horizontais
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                categories.forEach { category ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category.uppercase(),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        )
                        if (selectedCategory == category) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(2.dp)
                                    .background(Color.Black)
                            )
                        }
                    }
                }
            }

            // Mensagens de erro ou lista de notícias
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (newsList.isEmpty()) {
                Text(
                    text = "A carregar notícias...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(newsList) { news ->
                        NewsItem(news = news) {
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!news.multimedia.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(news.multimedia[0].url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = news.abstract,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}
