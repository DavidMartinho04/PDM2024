package com.example.loja_pdm.presentation.purchase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.loja_pdm.ui.components.AppHeader
import com.example.loja_pdm.presentation.viewmodels.Cart
import com.example.loja_pdm.presentation.viewmodels.Purchase
import com.example.loja_pdm.ui.components.AppDrawer
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PurchaseHistoryScreen(userEmail: String, navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val purchaseList = remember { mutableStateOf<List<Purchase>>(listOf()) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cores baseadas no estilo definido
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF)    // Branco
    val darkGrayColor = Color(0xFF333333) // Cinza escuro

    // Função para formatar a data
    fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    // Buscar dados da Firestore
    LaunchedEffect(userEmail) {
        db.collection("compras")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { result ->
                val purchases = result.documents.mapNotNull { document ->
                    val cartItemsList = document.get("cartItems") as? List<Map<String, Any>>
                    val cartItems = cartItemsList?.map { cartItem ->
                        Cart(
                            id = (cartItem["id"] as Long).toInt(),
                            name = cartItem["name"] as String,
                            imgUrl = cartItem["imgUrl"] as String,
                            preco = (cartItem["preco"] as Double),
                            quantity = (cartItem["quantity"] as Long).toInt(),
                            size = cartItem["size"] as String
                        )
                    } ?: listOf()

                    Purchase(
                        userEmail = document.getString("userEmail") ?: "",
                        name = document.getString("name") ?: "",
                        phone = document.getString("phone") ?: "",
                        address = document.getString("address") ?: "",
                        postalCode = document.getString("postalCode") ?: "",
                        paymentMethod = document.getString("paymentMethod") ?: "",
                        mbwayPhone = document.getString("mbwayPhone"),
                        purchaseDate = document.getTimestamp("purchaseDate") ?: Timestamp.now(),
                        cartItems = cartItems
                    )
                }
                purchaseList.value = purchases.sortedByDescending { it.purchaseDate.toDate() }
            }
            .addOnFailureListener {
                println("Erro ao carregar histórico de compras.")
            }
    }

    AppDrawer(drawerState = drawerState, navController = navController, scope = scope)
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkGrayColor)
        ) {
            AppHeader(
                navController = navController,
                onFavoriteClick = { navController.navigate("favorites") },
                onCartClick = { navController.navigate("cart") },
                onMenuClick = { scope.launch { drawerState.open() } }
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        text = "Histórico de Compras",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                items(purchaseList.value) { purchase ->
                    Spacer(modifier = Modifier.height(15.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(darkGrayColor, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Data: ${formatDate(purchase.purchaseDate)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Nome: ${purchase.name}", color = whiteColor)
                        Text("Email: ${purchase.userEmail}", color = whiteColor)
                        Text("Morada: ${purchase.address}", color = whiteColor)
                        Text("Código Postal: ${purchase.postalCode}", color = whiteColor)
                        Text("Telefone: ${purchase.phone}", color = whiteColor)
                        Text("Método de Pagamento: ${purchase.paymentMethod}", color = whiteColor)
                        Spacer(modifier = Modifier.height(35.dp))

                        Text(
                            text = "Artigos Comprados:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = primaryColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mostrar cada item comprado com imagem
                        purchase.cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(Color.DarkGray, RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagem do produto
                                Image(
                                    painter = rememberAsyncImagePainter(item.imgUrl),
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // Detalhes do produto mais espaçados
                                Column {
                                    Text(
                                        text = "${item.name} - Tamanho: ${item.size}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = whiteColor
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Quantidade: ${item.quantity}",
                                        fontSize = 15.sp,
                                        color = whiteColor
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Preço: €${item.preco}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryColor
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}