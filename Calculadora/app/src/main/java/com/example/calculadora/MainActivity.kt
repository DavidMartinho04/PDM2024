package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.calculadora.ui.CalculatorLayout
import com.example.calculadora.theme.CalculadoraTheme

/**
 * Atividade principal da aplicação.
 * Responsável por configurar a interface gráfica da calculadora.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ativa o modo "Edge-to-Edge" para uma experiência mais imersiva
        enableEdgeToEdge()

        // Define o conteúdo da interface gráfica
        setContent {
            // Aplica o tema personalizado da calculadora
            CalculadoraTheme {
                // O Scaffold é usado para estruturar o layout da aplicação
                Scaffold(
                    modifier = Modifier.fillMaxSize() // O layout ocupa o ecrã inteiro
                ) { innerPadding ->
                    // Chama o layout principal da calculadora, com o padding interno gerado pelo Scaffold
                    CalculatorLayout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
