package com.example.loja_pdm.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.loja_pdm.presentation.createaccount.CreateAccountScreen
import com.example.loja_pdm.presentation.createaccount.FinalizeAccountScreen
import com.example.loja_pdm.presentation.product.DetailProductScreen
import com.example.loja_pdm.presentation.product.FavoritesScreen
import com.example.loja_pdm.presentation.login.LoginScreen
import com.example.loja_pdm.presentation.menu.ShoeMenu
import com.example.loja_pdm.presentation.viewmodels.UserViewModel

@Composable
fun AppNavigation(navController: NavHostController, userViewModel: UserViewModel) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login", // Inicia no login
            modifier = Modifier.padding(innerPadding)
        ) {
            // Ecrã de Login
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { email ->
                        // Atualiza o email no UserViewModel após login
                        userViewModel.setEmail(email)
                        navController.navigate("menu") {
                            popUpTo("login") { inclusive = true } // Remove login da pilha de navegação
                        }
                    },
                    onCreateAccountClick = {
                        navController.navigate("createAccount")
                    }
                )
            }

            // Ecrã de Criação de Conta
            composable("createAccount") {
                CreateAccountScreen(
                    onNextClick = {
                        navController.navigate("finalizeAccount")
                    },
                    userViewModel = userViewModel
                )
            }

            // Ecrã de Finalização da Conta
            composable("finalizeAccount") {
                FinalizeAccountScreen(
                    onCreateAccountClick = {
                        navController.navigate("login")
                    },
                    userViewModel = userViewModel
                )
            }

            // Ecrã do Menu
            composable("menu") {
                // Passa o UserViewModel para garantir o acesso ao email
                ShoeMenu(navController = navController, userViewModel = userViewModel)
            }

            // Ecrã de Categoria Homem
            composable("men") {
                // MenScreen() // Implementação futura
            }

            // Ecrã de Categoria Mulher
            composable("women") {
                // WomenScreen() // Implementação futura
            }

            // Ecrã de Categoria Criança
            composable("kids") {
                // KidsScreen() // Implementação futura
            }

            // Ecrã de Detalhes do Produto
            composable(
                route = "productDetail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                DetailProductScreen(
                    navController = navController,
                    productId = productId,
                    userViewModel = userViewModel // Passa o UserViewModel
                )
            }

            // Ecrã de Favoritos
            composable("favorites") {
                FavoritesScreen(
                    userEmail = userViewModel.email,
                    navController = navController // Passa o NavController
                )
            }
        }
    }
}
