package com.example.calculadora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Componente para exibir o valor atual no visor da calculadora.
 *
 * @param value O valor a ser mostrado no visor.
 */
@Composable
fun CalculatorDisplay(value: String) {
    Box(
        modifier = Modifier
            .width(350.dp) // Define a largura do visor
            .height(100.dp) // Define a altura do visor
            .background(
                Color(193, 230, 193, 170), // Cor de fundo do visor com opacidade
                shape = RoundedCornerShape(16.dp) // Bordas arredondadas
            )
            .padding(16.dp) // Espaçamento interno do visor
    ) {
        // Texto exibido no visor
        Text(
            text = value, // Texto dinâmico com o valor atual
            fontSize = 64.sp, // Tamanho da fonte
            fontWeight = FontWeight.Bold, // Negrito para maior destaque
            fontFamily = FontFamily.Monospace, // Fonte monoespaçada (para uniformidade)
            color = Color.Black, // Cor do texto
            modifier = Modifier.align(Alignment.CenterEnd) // Alinha o texto à direita
        )
    }
}
