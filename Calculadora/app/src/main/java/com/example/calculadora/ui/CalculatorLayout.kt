package com.example.calculadora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calculadora.logic.updateState

/**
 * Componente principal do layout da calculadora.
 * Contém o visor e os botões da calculadora, gerindo os estados necessários para realizar cálculos.
 */
@Composable
fun CalculatorLayout(modifier: Modifier = Modifier) {
    // Estados da calculadora com MutableState
    val displayValue = remember { mutableStateOf("0") } // Valor atual exibido no visor
    val firstOperand = remember { mutableStateOf<Int?>(null) } // Primeiro operando
    val operator = remember { mutableStateOf<String?>(null) } // Operador atual (+, -, x, ÷)
    val isNextOperand = remember { mutableStateOf(false) } // Indica se o próximo número substitui o visor

    // Estrutura do layout da calculadora
    Column(
        modifier = modifier
            .fillMaxSize() // Ocupa o tamanho total disponível
            .background(Color.DarkGray) // Cor de fundo
            .padding(bottom = 32.dp), // Espaçamento inferior
        verticalArrangement = Arrangement.Bottom, // Alinha os elementos na parte inferior
        horizontalAlignment = Alignment.CenterHorizontally // Alinha os elementos ao centro horizontalmente
    ) {
        // Visor da calculadora que exibe o valor atual
        CalculatorDisplay(displayValue.value)

        // Espaçamento entre o visor e os botões
        Spacer(modifier = Modifier.height(35.dp))

        // Primeira linha de botões (MRC, M-, M+, ON)
        RowButtonRow(listOf("MRC", "M-", "M+", "ON"), redButton = 3, onButtonClick = {})

        // Segunda linha de botões (√, %, +/-, C)
        RowButtonRow(listOf("√", "%", "+/-", "C"), redButton = 3, onButtonClick = {
            updateState(it, displayValue, firstOperand, operator, isNextOperand)
        })

        // Terceira linha de botões (7, 8, 9, ÷)
        RowButtonRow(listOf("7", "8", "9", "÷"), onButtonClick = {
            updateState(it, displayValue, firstOperand, operator, isNextOperand)
        })

        // Quarta linha de botões (4, 5, 6, x)
        RowButtonRow(listOf("4", "5", "6", "x"), onButtonClick = {
            updateState(it, displayValue, firstOperand, operator, isNextOperand)
        })

        // Quinta linha de botões (1, 2, 3, -)
        RowButtonRow(listOf("1", "2", "3", "-"), onButtonClick = {
            updateState(it, displayValue, firstOperand, operator, isNextOperand)
        })

        // Sexta linha de botões (0, ., =, +)
        RowButtonRow(listOf("0", ".", "=", "+"), onButtonClick = {
            updateState(it, displayValue, firstOperand, operator, isNextOperand)
        })
    }
}
