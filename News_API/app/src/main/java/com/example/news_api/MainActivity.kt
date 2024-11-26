package com.example.news_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.news_api.presentation.NewsApp

/**
 * Classe principal da aplicação.
 *
 * Esta classe é responsável por iniciar a aplicação e renderizar a interface do utilizador
 * através do Jetpack Compose. A `MainActivity` serve como ponto de entrada para a aplicação.
 */
class MainActivity : ComponentActivity() {

    /**
     * Método chamado quando a atividade é criada.
     *
     * @param savedInstanceState Estado previamente salvo da atividade (usado para restaurar dados em
     * caso de recriação, por exemplo, mudança de orientação).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o conteúdo da atividade utilizando o Jetpack Compose
        setContent {
            // Chama o composable principal da aplicação que define o sistema de navegação
            NewsApp()
        }
    }
}
