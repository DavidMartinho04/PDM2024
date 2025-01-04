package com.example.calculadora.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente para criar uma linha de botões na calculadora.
 *
 * @param buttonLabels Lista de rótulos para os botões nesta linha (ex.: ["7", "8", "9", "÷"]).
 * @param redButton Índice do botão que deve ser vermelho (se nenhum for vermelho, use -1).
 * @param onButtonClick Função a ser chamada quando qualquer botão for pressionado, recebendo o texto do botão.
 */
@Composable
fun RowButtonRow(buttonLabels: List<String>, redButton: Int = -1, onButtonClick: (String) -> Unit) {
    // Criação de uma linha horizontal (Row) para os botões
    Row(
        modifier = Modifier
            .fillMaxWidth() // A linha ocupa toda a largura disponível
            .padding(4.dp), // Espaçamento ao redor da linha
        horizontalArrangement = Arrangement.SpaceEvenly // Distribui os botões de forma uniforme
    ) {
        // Iterar sobre os rótulos dos botões e criar um botão para cada um
        for ((index, label) in buttonLabels.withIndex()) {
            CalculatorButton(
                label = label, // Define o texto do botão
                isRed = index == redButton, // Define se o botão deve ser vermelho com base no índice
                onClick = onButtonClick // Passa a função de clique do botão
            )
        }
    }
}
