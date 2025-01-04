package com.example.loja_pdm.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.loja_pdm.data.models.Product
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FavoritesScreen(
    userEmail: String,
    navController: NavHostController
) {
    val db = FirebaseFirestore.getInstance()
    val favoriteProducts = remember { mutableStateOf<List<Product>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val primaryColor = Color(0xFFFF6F00)  // Laranja

    // Buscar os produtos favoritos
    LaunchedEffect(userEmail) {
        db.collection("favoritos")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val favoriteIds = result.documents.mapNotNull { it.getLong("productId")?.toInt() }
                if (favoriteIds.isNotEmpty()) {
                    db.collection("artigos")
                        .whereIn("id", favoriteIds)
                        .get()
                        .addOnSuccessListener { articles ->
                            val products = articles.documents.mapNotNull { doc ->
                                Product(
                                    id = doc.getLong("id")?.toInt() ?: 0,
                                    name = doc.getString("name") ?: "",
                                    imgUrl = doc.getString("img") ?: "",
                                    cor = doc.getString("cor") ?: "",
                                    marca = doc.getString("marca") ?: "",
                                    modelo = doc.getString("modelo") ?: "",
                                    preco = doc.getDouble("preco") ?: 0.0
                                )
                            }
                            favoriteProducts.value = products
                        }
                }
                isLoading.value = false
            }
    }

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .padding(horizontal = 16.dp)
    ) {
        AppHeader(navController = navController)
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
                                favoriteProducts.value = favoriteProducts.value.filterNot { it.id == productId }
                            }
                        }
                    )
                }
            }
        }
    }
}
