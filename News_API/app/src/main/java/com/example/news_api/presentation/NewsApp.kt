package com.example.news_api.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.news_api.presentation.news_detail.NewsDetailScreen
import com.example.news_api.presentation.news_list.NewsListScreen

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "newsList") {
        composable("newsList") {
            NewsListScreen(navController)
        }
        composable("newsDetail/{title}/{abstract}/{imageUrl}/{articleUrl}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val abstract = backStackEntry.arguments?.getString("abstract") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")
            val articleUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
            NewsDetailScreen(title, abstract, imageUrl, articleUrl)
        }
    }
}
