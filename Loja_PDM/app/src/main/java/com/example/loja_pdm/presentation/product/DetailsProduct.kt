package com.example.loja_pdm.presentation.product

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.loja_pdm.data.firebase.fetchProductById
import com.example.loja_pdm.data.firebase.isProductFavorite
import com.example.loja_pdm.presentation.viewmodels.Product
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.example.loja_pdm.ui.components.AppDrawer
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun DetailProductScreen(
    navController: NavHostController,
    productId: Int,
    userViewModel: UserViewModel
) {
    val userEmail = userViewModel.email
    val primaryColor = Color(0xFFFF6F00)
    val whiteColor = Color(0xFFFFFFFF)
    val blackColor = Color(0xFF333333)
    val context = LocalContext.current // Para o Toast
    val isFavorite = remember { mutableStateOf(false) }
    val selectedSize = remember { mutableStateOf("") }
    val product = remember { mutableStateOf<Product?>(null) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        fetchProductById(
            productId = productId,
            onSuccess = { fetchedProduct ->
                product.value = fetchedProduct
                if (fetchedProduct != null) {
                    isProductFavorite(
                        userEmail = userEmail,
                        productId = productId,
                        onSuccess = { isFav -> isFavorite.value = isFav },
                        onFailure = { exception ->
                            Toast.makeText(
                                context,
                                "Erro ao verificar favoritos: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            },
            onFailure = { exception ->
                Toast.makeText(
                    context,
                    "Erro ao buscar produto: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    AppDrawer(drawerState = drawerState, navController = navController, scope = scope)
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(blackColor)
        ) {
            AppHeader(
                navController = navController,
                onFavoriteClick = { navController.navigate("favorites") },
                onCartClick = { navController.navigate("cart") },
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            product.value?.let { produto ->
                Spacer(modifier = Modifier.height(16.dp))

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
                                favoritosCollection
                                    .whereEqualTo("userEmail", userEmail)
                                    .whereEqualTo("productId", productId)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            document.reference.delete()
                                        }
                                        isFavorite.value = false
                                        Toast.makeText(
                                            context,
                                            "Removido dos favoritos!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                val favoritoData =
                                    mapOf("userEmail" to userEmail, "productId" to productId)
                                favoritosCollection.add(favoritoData).addOnSuccessListener {
                                    isFavorite.value = true
                                    Toast.makeText(
                                        context,
                                        "Adicionado aos favoritos!",
                                        Toast.LENGTH_SHORT
                                    ).show()
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

                Text(
                    text = produto.modelo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = whiteColor,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )

                Text(
                    text = "Preço: € ${produto.preco}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = whiteColor,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    val sizes =
                        listOf("EU 36", "EU 37", "EU 38", "EU 39", "EU 40", "EU 41", "EU 42")
                    items(sizes) { size ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedSize.value == size) primaryColor else Color.DarkGray)
                                .clickable { selectedSize.value = size }
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Text(text = size, color = whiteColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedSize.value.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Selecione um tamanho antes de adicionar ao carrinho.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        val db = FirebaseFirestore.getInstance()
                        val carrinhoCollection = db.collection("carrinhos")

                        carrinhoCollection
                            .whereEqualTo("userEmail", userEmail)
                            .whereEqualTo("productId", productId)
                            .whereEqualTo("tamanho", selectedSize.value)
                            .get()
                            .addOnSuccessListener { result ->
                                if (result.isEmpty) {
                                    val carrinhoData = mapOf(
                                        "productId" to productId,
                                        "userEmail" to userEmail,
                                        "tamanho" to selectedSize.value,
                                        "quantidade" to 1
                                    )

                                    carrinhoCollection
                                        .add(carrinhoData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Produto adicionado ao carrinho!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context,
                                                "Erro ao adicionar: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                } else {
                                    val documentId = result.documents.first().id
                                    val existingQuantity =
                                        result.documents.first().getLong("quantidade")?.toInt() ?: 1

                                    carrinhoCollection
                                        .document(documentId)
                                        .update("quantidade", existingQuantity + 1)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Quantidade atualizada no carrinho!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context,
                                                "Erro ao atualizar: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "Erro ao verificar o carrinho: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 16.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Adicionar ao carrinho",
                        color = whiteColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
