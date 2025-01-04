package com.example.calculadora.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

/**
 * Componente de botão para a calculadora.
 *
 * @param label O texto a ser exibido no botão (número ou operador).
 * @param isRed Define se o botão deve ter a cor vermelha (usado para botões de limpar ou operadores especiais).
 * @param onClick Ação a ser executada quando o botão for clicado, recebendo o texto do botão como entrada.
 */
@Composable
fun CalculatorButton(label: String, isRed: Boolean = false, onClick: (String) -> Unit) {
    // Botão principal da calculadora
    Button(
        onClick = { onClick(label) }, // Chama a função onClick passando o texto do botão
        modifier = Modifier
            .size(90.dp) // Define o tamanho do botão
            .padding(3.dp), // Espaçamento ao redor do botão
        shape = RoundedCornerShape(10.dp), // Define os cantos arredondados do botão
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRed) Color.Red else Color.Black, // Cor do fundo (vermelho ou preto)
            contentColor = Color.White // Cor do texto
        )
    ) {
        // Texto exibido no botão
        Text(
            text = label, // Texto do botão (número ou operador)
            fontSize = 16.sp, // Tamanho da fonte
            fontWeight = FontWeight.Bold // Peso da fonte (negrito)
        )
    }
}
