package com.example.loja_pdm.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.loja_pdm.ui.components.AppHeader
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loja_pdm.R
import com.example.loja_pdm.data.models.Product
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ShoeMenu(navController: NavHostController, userViewModel: UserViewModel) {
    val userEmail = userViewModel.email // Obtém o email do utilizador a partir do ViewModel
    val db = FirebaseFirestore.getInstance()
    val artigosList = remember { mutableStateOf<List<Product>>(listOf()) }

    // Cores definidas para a interface
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val secondaryColor = Color(0xFF6F6F6F)  // Cinza escuro
    val whiteColor = Color(0xFFFFFFFF) // Branco
    val textFieldBackground = Color(0xFF333333) // Fundo preto dos campos
    val blackColor = Color(0xFF181818) // Preto

    // Buscar dados da coleção "artigos" ao iniciar
    db.collection("artigos")
        .get()
        .addOnSuccessListener { result ->
            val produtos = result.map { document ->
                Product(
                    id = document.getDouble("id")?.toInt() ?: 0,
                    name = document.getString("name") ?: "",
                    imgUrl = document.getString("img") ?: "",
                    cor = document.getString("cor") ?: "",
                    marca = document.getString("marca") ?: "",
                    modelo = document.getString("modelo") ?: "",
                    preco = document.getDouble("preco") ?: 0.0
                )
            }
            artigosList.value = produtos
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(textFieldBackground)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(
            navController = navController,
            onFavoriteClick = { navController.navigate("favorites") },
            onCartClick = { /* Ação do carrinho */ },
            onMenuClick = { /* Ação do menu */ }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Título da seção
        Text(
            text = "Tendências desta semana",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = primaryColor,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Carrossel de produtos (sapatilhas)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(artigosList.value) { produto ->
                Column(
                    modifier = Modifier
                        .width(240.dp)
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate("productDetail/${produto.id}") }, // Navega ao detalhe
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagem do produto
                    Box(
                        modifier = Modifier
                            .size(230.dp)
                            .background(
                                Color.Gray,
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(produto.imgUrl),
                            contentDescription = produto.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Nome do produto
                    Text(
                        text = produto.modelo,
                        fontWeight = FontWeight.Bold,
                        color = whiteColor,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Preço do produto
                    Text(
                        text = "€ ${produto.preco}",
                        color = whiteColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp)) // Espaço entre os dois carrosséis

        // Novo título
        Text(
            text = "Explora mais",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = primaryColor,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Novo carrossel de categorias
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                listOf(
                    Pair("Homem", R.drawable.homem),
                    Pair("Mulher", R.drawable.mulher),
                    Pair("Criança", R.drawable.kids)
                )
            ) { category ->
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { navController.navigate("category/${category.first}") },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Imagem da categoria
                    Image(
                        painter = painterResource(category.second),
                        contentDescription = category.first,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Botão sobreposto
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.first,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = whiteColor
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Créditos no rodapé
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Instituto Politécnico do Cávado e do Ave",
                    fontSize = 14.sp,
                    color = secondaryColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aluno Nº 25620",
                    fontSize = 12.sp,
                    color = secondaryColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Programação de Dispositivos Móveis 2024",
                    fontSize = 12.sp,
                    color = secondaryColor
                )
            }
        }
    }
}
