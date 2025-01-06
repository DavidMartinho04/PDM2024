package com.example.loja_pdm.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

data class Cart(
    val id: Int = 0, // ID do produto
    val name: String = "", // Nome do produto
    val imgUrl: String = "", // URL da imagem
    val preco: Double = 0.0, // Preço do produto
    val quantity: Int = 1, // Quantidade no carrinho
    val size: String = "Único" // Tamanho selecionado
)

class CartViewModel : ViewModel() {

    private val _cartProducts = mutableStateOf<List<Cart>>(emptyList())
    val cartProducts: State<List<Cart>> = _cartProducts

    fun updateCartProducts(products: List<Cart>) {
        _cartProducts.value = products
    }

    fun clearCart() {
        _cartProducts.value = emptyList()
    }
}

