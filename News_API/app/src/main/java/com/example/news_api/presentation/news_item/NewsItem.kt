package com.example.news_api.presentation.news_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.news_api.data.remote.model.NewsDto

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
