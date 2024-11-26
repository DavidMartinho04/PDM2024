package com.example.news_api.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.news_api.presentation.news_detail.NewsDetailScreen
import com.example.news_api.presentation.news_list.NewsListScreen

/**
 * Função principal da aplicação que configura o sistema de navegação entre os diferentes ecrãs.
 *
 * Esta função utiliza o Jetpack Navigation Compose para gerir as transições entre os ecrãs da lista
 * de notícias e os detalhes de uma notícia selecionada.
 */
@Composable
fun NewsApp() {
    // Controlador de navegação que gere o estado e as transições entre os destinos.
    val navController = rememberNavController()

    /**
     * Configuração do NavHost, que define os destinos (ecrãs) e as suas rotas.
     *
     * @param navController Controlador de navegação.
     * @param startDestination Define a rota inicial (ponto de entrada na aplicação).
     */
    NavHost(navController = navController, startDestination = "newsList") {
        // Destino para o ecrã da lista de notícias
        composable("newsList") {
            NewsListScreen(navController) // Chama o composable responsável por mostrar a lista de notícias
        }

        /**
         * Destino para o ecrã de detalhes da notícia.
         *
         * A rota inclui dois parâmetros: "title" e "abstract", que são extraídos da pilha de navegação
         * para serem passados ao ecrã de detalhes.
         */
        composable("newsDetail/{title}/{abstract}") { backStackEntry ->
            // Obtém os argumentos passados pela navegação
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val abstract = backStackEntry.arguments?.getString("abstract") ?: ""

            // Chama o composable responsável por mostrar os detalhes da notícia
            NewsDetailScreen(title, abstract)
        }
    }
}
