package com.example.loja_pdm.data.models

// Product.kt
data class Product(
    val id: Number = 0,
    val name: String = "",
    val imgUrl: String = "",
    val cor: String = "",
    val marca: String = "",
    val modelo: String = "",
    val preco: Double = 0.0
)