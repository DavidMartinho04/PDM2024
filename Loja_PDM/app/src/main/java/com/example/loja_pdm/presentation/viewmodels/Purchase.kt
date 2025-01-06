package com.example.loja_pdm.presentation.viewmodels

import com.google.firebase.Timestamp

data class Purchase(
    val userEmail: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val postalCode: String = "",
    val paymentMethod: String = "",
    val cardNumber: String? = null,
    val cvv: String? = null,
    val mbwayPhone: String? = null,
    val purchaseDate: Timestamp = Timestamp.now(),
    val cartItems: List<Cart> = listOf()
)