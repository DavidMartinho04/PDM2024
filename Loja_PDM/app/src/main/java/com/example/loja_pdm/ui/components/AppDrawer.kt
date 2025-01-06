package com.example.loja_pdm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val whiteColor = Color(0xFFFFFFFF)    // Branco
    val textFieldBackground = Color(0xFF333333) // Preto

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .background(textFieldBackground)
                    .padding(16.dp)
            ) {
                Text("Bem Vindo", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                Spacer(modifier = Modifier.height(24.dp))

                DrawerItem("Histórico de Compras", Icons.Default.History, whiteColor, primaryColor) {
                    navController.navigate("purchaseHistory")
                    scope.launch { drawerState.close() }
                }
                DrawerItem("Homem", Icons.Default.Male, whiteColor, primaryColor) {
                    navController.navigate("man")
                    scope.launch { drawerState.close() }
                }
                DrawerItem("Mulher", Icons.Default.Female, whiteColor, primaryColor) {
                    navController.navigate("woman")
                    scope.launch { drawerState.close() }
                }
                DrawerItem("Criança", Icons.Default.ChildCare, whiteColor, primaryColor) {
                    navController.navigate("kids")
                    scope.launch { drawerState.close() }
                }
                DrawerItem("Terminar Sessão", Icons.Default.ExitToApp, whiteColor, primaryColor) {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("menu") { inclusive = true }
                    }
                    scope.launch { drawerState.close() }
                }
            }
        },
        content = content
    )
}