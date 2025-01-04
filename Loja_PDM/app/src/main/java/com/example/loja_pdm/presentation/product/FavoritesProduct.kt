package com.example.loja_pdm.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.loja_pdm.data.models.Product

// Componente para mostrar um item de produto favorito
@Composable
fun FavoriteProductItem(
    product: Product,
    onRemoveFavorite: (Int) -> Unit // Função de callback para remover dos favoritos
) {
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF) // Branco

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagem do produto
        Image(
            painter = rememberAsyncImagePainter(product.imgUrl),
            contentDescription = product.modelo,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Detalhes do produto
        Column(
            modifier = Modifier.weight(1f) // Ocupa o espaço restante
        ) {
            Text(
                text = product.modelo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = whiteColor
            )
            Text(
                text = "€ ${product.preco}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = whiteColor
            )
        }

        // Botão de remover (ícone de lixo)
        IconButton(
            onClick = { onRemoveFavorite(product.id.toInt()) }, // Converte para Int antes de passar
            modifier = Modifier.size(24.dp) // Tamanho do botão
        ) {
            Icon(
                imageVector = Icons.Default.Delete, // Ícone de lixo nativo
                contentDescription = "Remover dos favoritos",
                tint = primaryColor // Cor do ícone
            )
        }
    }
}


