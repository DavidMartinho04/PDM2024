package com.example.loja_pdm.presentation.cart

import android.content.Context
import android.content.Intent
import com.example.loja_pdm.presentation.viewmodels.Cart

// Função para gerar o texto do carrinho com o título incluído
fun generateCartText(cartItems: List<Cart>): String {
    if (cartItems.isEmpty()) return "O carrinho está vazio."

    return buildString {
        append("Carrinho de Compras:\n\n")  // Título adicionado aqui
        append(cartItems.joinToString(separator = "\n") { product ->
            "${product.name} - Tamanho: ${product.size} - Quantidade: ${product.quantity} - Preço: €${"%.2f".format(product.preco)}"
        })
        append("\n\nTotal: €${"%.2f".format(cartItems.sumOf { it.preco * it.quantity })}")
    }
}

// Função para partilhar o carrinho com o título incluído
fun shareCart(context: Context, cartItems: List<Cart>) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, generateCartText(cartItems))
    }
    context.startActivity(Intent.createChooser(shareIntent, "Partilhar carrinho via:"))
}
