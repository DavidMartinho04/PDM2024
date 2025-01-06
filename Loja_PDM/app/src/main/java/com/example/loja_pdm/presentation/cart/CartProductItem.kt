package com.example.loja_pdm.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.loja_pdm.presentation.viewmodels.Cart

@Composable
fun CartProductItem(
    product: Cart,
    onRemoveItem: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit
) {
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF)   // Branco

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagem do produto
            Image(
                painter = rememberAsyncImagePainter(product.imgUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Detalhes do produto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Nome do produto com validação
                    Text(
                        text = "Modelo: ${product.name.ifEmpty { "Sem nome" }}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = primaryColor,
                        modifier = Modifier.weight(1f)
                    )

                    // Botão de remover (ícone de lixo)
                    IconButton(
                        onClick = onRemoveItem,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover produto",
                            tint = primaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Tamanho do produto
                Text(
                    text = "Tamanho: ${product.size.ifEmpty { "Único" }}",
                    fontSize = 14.sp,
                    color = whiteColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Quantidade e Preço
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Quantidade: ${product.quantity}",
                        fontSize = 14.sp,
                        color = whiteColor
                    )
                    Text(
                        text = "€ ${"%.2f".format(product.preco * product.quantity)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = primaryColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botões de incremento e decremento com controlo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botão de diminuir quantidade
            IconButton(
                onClick = {
                    if (product.quantity > 1) onDecreaseQuantity()
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Diminuir quantidade",
                    tint = primaryColor
                )
            }

            // Quantidade atual
            Text(
                text = "${product.quantity}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = whiteColor
            )

            // Botão de aumentar quantidade
            IconButton(
                onClick = onIncreaseQuantity,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Aumentar quantidade",
                    tint = primaryColor
                )
            }
        }
    }
}
