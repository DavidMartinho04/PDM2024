package com.example.loja_pdm.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loja_pdm.R
import com.example.loja_pdm.data.firebase.fetchArticles
import com.example.loja_pdm.presentation.viewmodels.Product
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.example.loja_pdm.ui.components.AppDrawer
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun ShoeMenu(navController: NavHostController, userViewModel: UserViewModel) {
    val userEmail = userViewModel.email
    val db = FirebaseFirestore.getInstance()
    val artigosList = remember { mutableStateOf<List<Product>>(listOf()) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cores
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val secondaryColor = Color(0xFF6F6F6F)  // Cinza escuro
    val whiteColor = Color(0xFFFFFFFF)
    val textFieldBackground = Color(0xFF333333)

    // Buscar os artigos ao iniciar
    LaunchedEffect(Unit) {
        fetchArticles(
            onSuccess = { produtos -> artigosList.value = produtos },
            onFailure = { exception -> println("Erro: ${exception.message}") }
        )
    }

    AppDrawer(drawerState = drawerState, navController = navController, scope = scope)

     {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(textFieldBackground)
                .verticalScroll(rememberScrollState())
        ) {
            AppHeader(
                navController = navController,
                onFavoriteClick = { navController.navigate("favorites") },
                onCartClick = { navController.navigate("cart") },
                onMenuClick = { scope.launch { drawerState.open() } }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Título da secção
            Text(
                text = "Tendências desta semana",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = primaryColor,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Carrossel de produtos
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
                            .clickable { navController.navigate("productDetail/${produto.id}") },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(230.dp)
                                .background(Color.Gray, RoundedCornerShape(16.dp))
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
                        Text(text = produto.modelo, color = whiteColor, fontWeight = FontWeight.Bold)
                        Text(text = "€ ${produto.preco}", color = whiteColor)
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
                            .clickable {
                                when (category.first) {
                                    "Homem" -> navController.navigate("man")
                                    "Mulher" -> navController.navigate("woman")
                                    "Criança" -> navController.navigate("kids")
                                }
                            },
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter = painterResource(category.second),
                            contentDescription = category.first,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
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
}
