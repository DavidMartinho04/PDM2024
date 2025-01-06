package com.example.loja_pdm.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.loja_pdm.presentation.viewmodels.Cart
import com.example.loja_pdm.ui.components.AppHeader
import com.google.firebase.firestore.FirebaseFirestore
import com.example.loja_pdm.data.firebase.adjustQuantity
import com.example.loja_pdm.data.firebase.removeItemFromCart
import com.example.loja_pdm.presentation.viewmodels.CartViewModel
import com.example.loja_pdm.ui.components.AppDrawer
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun CartScreen(
    userEmail: String,
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val db = FirebaseFirestore.getInstance()
    val isLoading = remember { mutableStateOf(true) }
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF)    // Branco
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Buscar os produtos no carrinho e atualizar o ViewModel
    LaunchedEffect(userEmail) {
        db.collection("carrinhos")
            .whereEqualTo("userEmail", userEmail)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Erro ao ouvir alterações no carrinho: $e")
                    isLoading.value = false
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val cartItems = snapshot.documents.mapNotNull { cartDoc ->
                        val productId =
                            cartDoc.getLong("productId")?.toInt() ?: return@mapNotNull null
                        val quantity = cartDoc.getLong("quantidade")?.toInt() ?: 1
                        val size = cartDoc.getString("tamanho") ?: "Único"
                        Triple(productId, quantity, size)
                    }
                    if (cartItems.isNotEmpty()) {
                        val productIds = cartItems.map { it.first }
                        db.collection("artigos")
                            .whereIn("id", productIds)
                            .get()
                            .addOnSuccessListener { articles ->
                                val products = cartItems.mapNotNull { (productId, quantity, size) ->
                                    val matchingDoc = articles.documents.find {
                                        it.getLong("id")?.toInt() == productId
                                    }
                                    matchingDoc?.let { doc ->
                                        Cart(
                                            id = productId,
                                            name = doc.getString("modelo") ?: "Produto sem nome",
                                            imgUrl = doc.getString("img") ?: "",
                                            preco = doc.getDouble("preco") ?: 0.0,
                                            quantity = quantity,
                                            size = size
                                        )
                                    }
                                }
                                cartViewModel.updateCartProducts(products)
                                isLoading.value = false
                            }
                            .addOnFailureListener { e ->
                                println("Erro ao buscar artigos: $e")
                                isLoading.value = false
                            }
                    } else {
                        cartViewModel.clearCart()
                        isLoading.value = false
                    }
                } else {
                    cartViewModel.clearCart()
                    isLoading.value = false
                }
            }
    }

    // Calcula o total
    val total = cartViewModel.cartProducts.value.sumOf { it.preco * it.quantity }

    AppDrawer(drawerState = drawerState, navController = navController, scope = scope)
    {

        // Layout principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF333333))
        ) {
            AppHeader(
                navController = navController,
                onFavoriteClick = { navController.navigate("favorites") },
                onCartClick = { navController.navigate("cart") },
                onMenuClick = { scope.launch { drawerState.open() } }
            )

            // Título e Ícone de Partilha Alinhados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Alinhamento entre o título e o ícone
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Carrinho de Compras",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = primaryColor
                )
                IconButton(onClick = {
                    shareCart(context, cartViewModel.cartProducts.value)
                }) {
                    Icon(
                        imageVector = Icons.Default.Share, // Ícone nativo
                        contentDescription = "Partilhar Carrinho",
                        tint = primaryColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "A carregar itens do carrinho...", color = Color.White)
                }
            } else if (cartViewModel.cartProducts.value.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "O carrinho está vazio!", color = Color.White)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(cartViewModel.cartProducts.value) { product ->
                            CartProductItem(
                                product = product,
                                onRemoveItem = {
                                    removeItemFromCart(
                                        product, userEmail
                                    )
                                },
                                onIncreaseQuantity = {
                                    adjustQuantity(
                                        product = product,
                                        userEmail = userEmail,
                                        increment = true
                                    )
                                },
                                onDecreaseQuantity = {
                                    adjustQuantity(
                                        product = product,
                                        userEmail = userEmail,
                                        increment = false
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Total do Carrinho
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = primaryColor
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "€ ${"%.2f".format(total)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            navController.navigate("checkoutForm")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp, vertical = 16.dp)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Finalizar Compra",
                            color = whiteColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}