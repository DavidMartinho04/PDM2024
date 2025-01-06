package com.example.loja_pdm.presentation.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.loja_pdm.data.firebase.fetchArticlesByType
import com.example.loja_pdm.presentation.viewmodels.Product
import com.example.loja_pdm.ui.components.AppDrawer
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun WomanScreen(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val manProductsList = remember { mutableStateOf<List<Product>>(listOf()) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cores
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF)    // Branco
    val textFieldBackground = Color(0xFF333333) // Preto

    LaunchedEffect(Unit) {
        fetchArticlesByType(
            tipo = "woman",
            onSuccess = { produtos -> manProductsList.value = produtos },
            onFailure = { exception -> println("Erro: ${exception.message}") }

        )
    }

    AppDrawer(drawerState = drawerState, navController = navController, scope = scope)
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(textFieldBackground)
                .padding(16.dp)
        ) {
            // Cabeçalho
            AppHeader(
                navController = navController,
                onFavoriteClick = { navController.navigate("favorites") },
                onCartClick = { navController.navigate("cart") },
                onMenuClick = { scope.launch { drawerState.open() } }
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Título
            Text(
                text = "Produtos para Mulher",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = primaryColor,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Grid de produtos 2 colunas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(manProductsList.value) { produto ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray, RoundedCornerShape(16.dp))
                            .padding(8.dp)
                            .clickable { navController.navigate("productDetail/${produto.id}") },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Imagem do produto
                        Image(
                            painter = rememberAsyncImagePainter(produto.imgUrl),
                            contentDescription = produto.name,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Nome do produto
                        Text(
                            text = produto.modelo,
                            fontWeight = FontWeight.Bold,
                            color = whiteColor,
                            fontSize = 16.sp
                        )

                        // Preço
                        Text(
                            text = "€ ${produto.preco}",
                            color = primaryColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
