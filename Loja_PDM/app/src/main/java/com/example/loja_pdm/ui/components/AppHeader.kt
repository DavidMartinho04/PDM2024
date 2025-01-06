package com.example.loja_pdm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loja_pdm.R

@Composable
fun AppHeader(
    navController: NavHostController,
    onFavoriteClick: ()  -> Unit = {
        // Navega para a tela de favoritos
        navController.navigate("favorites")
    },
    onCartClick: () -> Unit = {
        navController.navigate("cart")
    },
    onMenuClick: () -> Unit = {}
) {
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF) // Branco

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Cabeçalho com logo e ícones
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo da loja que agora navega para o menu principal
                Image(
                    painter = painterResource(id = R.drawable.urban_shoes),
                    contentDescription = "Logo da loja",
                    modifier = Modifier
                        .size(130.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            navController.navigate("menu") {
                                popUpTo("menu") { inclusive = true }
                            }
                        }
                )

                // Ícones do cabeçalho
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(y = (-30).dp) // Move os ícones para cima
                ) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorito",
                            tint = whiteColor
                        )
                    }
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrinho",
                            tint = whiteColor
                        )
                    }
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = whiteColor
                        )
                    }
                }
            }
        }

        // Categorias com navegação ajustada
        Row(
            modifier = Modifier
                .offset(y = (-40).dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Homem", "Mulher", "Criança").forEach { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        when (category) {
                            "Homem" -> navController.navigate("man")
                            "Mulher" -> navController.navigate("woman")
                            "Criança" -> navController.navigate("kids")
                        }
                    }
                ) {
                    Text(
                        text = category,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(2.dp)
                            .background(primaryColor)
                    )
                }
            }
        }
    }
}