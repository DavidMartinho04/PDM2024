package com.example.news_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.news_api.presentation.NewsApp

/**
 * Classe principal da aplicação.
 *
 * Esta classe é responsável por iniciar a aplicação e mostrar a interface do utilizador.
 *
 * A `MainActivity` serve como ponto de entrada para a aplicação.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o conteúdo da atividade
        setContent {
            // Chama o composable principal da aplicação
            NewsApp()
        }
    }
}
