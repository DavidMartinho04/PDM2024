package com.example.loja_pdm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    onCartClick: () -> Unit = {},
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
                // Logo da loja
                Image(
                    painter = painterResource(id = R.drawable.urban_shoes),
                    contentDescription = "Logo da loja",
                    modifier = Modifier
                        .size(130.dp)
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

        // Categorias
        Row(
            modifier = Modifier
                .offset(y = (-40).dp) // Move as categorias para cima
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Homem", "Mulher", "Criança").forEach { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        navController.navigate("category/$category") // Navega para a página da categoria
                    }
                ) {
                    // Texto da categoria
                    Text(
                        text = category,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = primaryColor
                    )

                    // Risco laranja abaixo do texto
                    Spacer(modifier = Modifier.height(4.dp)) // Espaço entre o texto e o risco
                    Box(
                        modifier = Modifier
                            .width(50.dp) // Largura do risco
                            .height(2.dp) // Altura do risco
                            .background(primaryColor) // Cor laranja
                    )
                }
            }
        }
    }
}
