package com.example.news_api.presentation.news_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Função composable que apresenta o ecrã de detalhes de uma notícia.
 *
 * Este ecrã mostra o título e o resumo (abstract) de uma notícia.
 *
 * @param title O título da notícia.
 * @param abstract O resumo (abstract) da notícia.
 */
@Composable
fun NewsDetailScreen(title: String, abstract: String) {
    // Coluna que organiza os elementos do ecrã verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize() // Preenche todo o espaço disponível
            .padding(16.dp) // Adiciona margens de 16dp em todos os lados
    ) {
        // Apresenta o título da notícia com estilo destacado
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium, // Estilo de título médio
            color = MaterialTheme.colorScheme.primary // Cor principal do tema
        )
        // Espaçamento vertical de 16dp entre o título e o resumo
        Spacer(modifier = Modifier.height(16.dp))
        // Apresenta o resumo da notícia com estilo de corpo
        Text(
            text = abstract,
            style = MaterialTheme.typography.bodyLarge, // Estilo de texto de corpo grande
            color = MaterialTheme.colorScheme.onSurface // Cor para textos em superfícies
        )
    }
}
