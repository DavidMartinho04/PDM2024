package com.example.loja_pdm.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.loja_pdm.data.models.Product
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailProductScreen(
    navController: NavHostController,
    productId: Int,
    userViewModel: UserViewModel
) {
    val userEmail = userViewModel.email // Obtém o email do utilizador autenticado
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF) // Branco
    val blackColor = Color(0xFF333333) // Preto
    val isFavorite = remember { mutableStateOf(false) } // Estado do coração

    // Estado para armazenar os detalhes do produto
    val product = remember { mutableStateOf<Product?>(null) }

    // Buscar o produto específico ao iniciar
    LaunchedEffect(productId) {
        val db = FirebaseFirestore.getInstance()

        // Carregar detalhes do produto
        db.collection("artigos")
            .whereEqualTo("id", productId)
            .get()
            .addOnSuccessListener { result ->
                val fetchedProduct = result.documents.firstOrNull()?.let { document ->
                    Product(
                        id = document.getLong("id")?.toInt() ?: 0,
                        name = document.getString("name") ?: "",
                        imgUrl = document.getString("img") ?: "",
                        cor = document.getString("cor") ?: "",
                        marca = document.getString("marca") ?: "",
                        modelo = document.getString("modelo") ?: "",
                        preco = document.getDouble("preco") ?: 0.0
                    )
                }
                product.value = fetchedProduct

                // Verificar se o produto está nos favoritos
                db.collection("favoritos")
                    .whereEqualTo("userEmail", userEmail)
                    .whereEqualTo("productId", productId)
                    .get()
                    .addOnSuccessListener { favResult ->
                        isFavorite.value = !favResult.isEmpty
                    }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blackColor)
    ) {
        // Cabeçalho reutilizado com categorias
        AppHeader(navController = navController)

        product.value?.let { produto ->
            Spacer(modifier = Modifier.height(16.dp))

            // Imagem do Produto com Ícone de Coração
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .height(260.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(produto.imgUrl),
                    contentDescription = produto.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = {
                        val db = FirebaseFirestore.getInstance()
                        val favoritosCollection = db.collection("favoritos")

                        if (isFavorite.value) {
                            // Remover dos favoritos
                            favoritosCollection
                                .whereEqualTo("userEmail", userEmail)
                                .whereEqualTo("productId", productId)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        document.reference.delete()
                                    }
                                    isFavorite.value = false
                                }
                        } else {
                            // Adicionar aos favoritos
                            val favoritoData = mapOf(
                                "userEmail" to userEmail,
                                "productId" to productId
                            )
                            favoritosCollection
                                .add(favoritoData)
                                .addOnSuccessListener {
                                    isFavorite.value = true
                                }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Adicionar aos favoritos",
                        tint = if (isFavorite.value) Color.Red else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nome do Produto
            Text(
                text = produto.modelo,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = whiteColor,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )

            // Preço do Produto
            Text(
                text = "Preço: € ${produto.preco}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = whiteColor,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Seleção de tamanhos
            Text(
                text = "Selecionar tamanho",
                fontWeight = FontWeight.Bold,
                color = whiteColor,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(listOf("EU 36", "EU 37", "EU 38", "EU 39", "EU 40", "EU 41", "EU 42")) { size ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray)
                            .clickable { }
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Text(text = size, color = whiteColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de Adicionar ao Carrinho
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp, vertical = 16.dp)
                    .height(50.dp)
            ) {
                Text(text = "Adicionar ao carrinho", color = whiteColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
