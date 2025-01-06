package com.example.loja_pdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.loja_pdm.presentation.navigation.AppNavigation
import com.example.loja_pdm.presentation.viewmodels.CartViewModel
import com.example.loja_pdm.presentation.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel by viewModels()
            val cartViewModel: CartViewModel by viewModels()

            AppNavigation(navController, userViewModel, cartViewModel)
        }
    }
}