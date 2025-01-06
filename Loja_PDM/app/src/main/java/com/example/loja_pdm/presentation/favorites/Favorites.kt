package com.example.loja_pdm.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loja_pdm.data.firebase.fetchFavoriteProducts
import com.example.loja_pdm.presentation.viewmodels.Product
import com.example.loja_pdm.ui.components.AppDrawer
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    userEmail: String,
    navController: NavHostController
) {
    val db = FirebaseFirestore.getInstance()
    val favoriteProducts = remember { mutableStateOf<List<Product>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(userEmail) {
        fetchFavoriteProducts(
            userEmail = userEmail,
            onSuccess = { products ->
                favoriteProducts.value = products
                isLoading.value = false
            },
            onFailure = { exception ->
                println("Erro ao buscar favoritos: \${exception.message}")
                isLoading.value = false
            }
        )
    }

    AppDrawer(drawerState = drawerState, navController = navController, scope = scope)
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF333333))
                .padding(horizontal = 16.dp)
        ) {
            AppHeader(
                navController = navController,
                onFavoriteClick = { navController.navigate("favorites") },
                onCartClick = { navController.navigate("cart") },
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Os Seus Favoritos",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = primaryColor,
                modifier = Modifier.padding(start = 16.dp)
            )

            if (favoriteProducts.value.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Nenhum favorito encontrado!", color = Color.White)
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteProducts.value) { product ->
                        FavoriteProductItem(
                            product = product,
                            onRemoveFavorite = { productId ->
                                // Lógica para remover o produto dos favoritos
                                db.collection("favoritos")
                                    .whereEqualTo("userEmail", userEmail)
                                    .whereEqualTo("productId", productId)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            document.reference.delete()
                                        }
                                        favoriteProducts.value =
                                            favoriteProducts.value.filterNot { it.id == productId }
                                    }
                            }
                        )
                    }
                }
            }
        }
    }
}
