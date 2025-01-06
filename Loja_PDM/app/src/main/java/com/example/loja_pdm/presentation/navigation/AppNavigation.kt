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
import com.example.loja_pdm.presentation.favorites.FavoritesScreen
import com.example.loja_pdm.presentation.login.LoginScreen
import com.example.loja_pdm.presentation.menu.ShoeMenu
import com.example.loja_pdm.presentation.cart.CartScreen
import com.example.loja_pdm.presentation.categories.KidsScreen
import com.example.loja_pdm.presentation.categories.ManScreen
import com.example.loja_pdm.presentation.categories.WomanScreen
import com.example.loja_pdm.presentation.checkout.FinalizePurchaseScreen
import com.example.loja_pdm.presentation.purchase.PurchaseHistoryScreen
import com.example.loja_pdm.presentation.viewmodels.CartViewModel
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController, userViewModel: UserViewModel, cartViewModel: CartViewModel) {
    val auth = FirebaseAuth.getInstance()

    // Verificar o estado de login e garantir a persistência
    val isLoggedIn = auth.currentUser != null
    if (isLoggedIn) {
        userViewModel.setEmail(auth.currentUser?.email ?: "")
    }

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "menu" else "login", // Garantia de autenticação
            modifier = Modifier.padding(innerPadding)
        ) {
            // Ecrã de Login
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { email ->
                        userViewModel.setEmail(email)
                        navController.navigate("menu") {
                            popUpTo("login") { inclusive = true }
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

            // Ecrã do Menu Principal
            composable("menu") {
                ShoeMenu(navController = navController, userViewModel = userViewModel)
            }

            // Ecrã de Categoria Homem
            composable("man") {
                ManScreen(navController = navController)
            }

            // Ecrã de Categoria Mulher
            composable("woman") {
                WomanScreen(navController = navController)
            }

            // Ecrã de Categoria Criança
            composable("kids") {
                KidsScreen(navController = navController)
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
                    userViewModel = userViewModel
                )
            }

            // Ecrã de Favoritos
            composable("favorites") {
                FavoritesScreen(
                    userEmail = userViewModel.email,
                    navController = navController
                )
            }

            // Tela do Carrinho de Compras
            composable("cart") {
                CartScreen(
                    userEmail = userViewModel.email,
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }

            // Tela de Finalizar Compra
            composable("checkoutForm") {
                FinalizePurchaseScreen(
                    userEmail = userViewModel.email,
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }

            // Tela de Histórico de Compras
            composable("purchaseHistory") {
                PurchaseHistoryScreen(
                    navController = navController,
                    userEmail = userViewModel.email
                )
            }
        }
    }
}
