package com.example.calculadora.logic

import androidx.compose.runtime.MutableState

/**
 * Função principal que atualiza o estado da calculadora com base na entrada do utilizador.
 * @param input Entrada do utilizador, como números, operadores ou comandos (C, =, etc.).
 * @param displayValue Estado que armazena o valor atual mostrado no visor.
 * @param firstOperand Estado que armazena o primeiro operando para operações matemáticas.
 * @param operator Estado que armazena o operador matemático atual (+, -, x, ÷).
 * @param isNextOperand Estado que indica se o próximo número deve substituir o valor atual do visor.
 */
fun updateState(
    input: String,
    displayValue: MutableState<String>,
    firstOperand: MutableState<Int?>,
    operator: MutableState<String?>,
    isNextOperand: MutableState<Boolean>
) {
    when {
        // Caso o input seja um número
        input in "0".."9" -> {
            if (isNextOperand.value) {
                // Substituir o valor atual do visor
                displayValue.value = input
                isNextOperand.value = false
            } else {
                // Concatenar o novo número ao valor atual do visor
                displayValue.value = if (displayValue.value == "0") input else displayValue.value + input
            }
        }
        // Caso o input seja um operador (+, -, x, ÷)
        input in listOf("+", "-", "x", "÷") -> {
            if (firstOperand.value != null && operator.value != null) {
                // Se já houver um operando e operador, calcular o resultado
                val secondOperand = displayValue.value.toIntOrNull()
                val result = calculateResult(firstOperand.value!!, secondOperand ?: 0, operator.value!!)
                firstOperand.value = result // Guardar o resultado como o novo primeiro operando
                displayValue.value = result.toString() // Atualizar o visor com o resultado
            } else {
                // Guardar o valor atual do visor como o primeiro operando
                firstOperand.value = displayValue.value.toIntOrNull()
            }
            operator.value = input // Guardar o operador atual
            isNextOperand.value = true // Indicar que o próximo número substituirá o valor atual
        }
        // Caso o input seja o operador "="
        input == "=" -> {
            if (firstOperand.value != null && operator.value != null) {
                // Calcular o resultado com base no primeiro operando, operador e segundo operando
                val secondOperand = displayValue.value.toIntOrNull()
                val result = calculateResult(firstOperand.value!!, secondOperand ?: 0, operator.value!!)
                displayValue.value = result.toString() // Mostrar o resultado no visor
                firstOperand.value = null // Reset ao primeiro operando
                operator.value = null // Reset ao operador
            }
        }
        // Caso o input seja "C" (limpar)
        input == "C" -> {
            displayValue.value = "0" // Reset do visor
            firstOperand.value = null // Reset ao primeiro operando
            operator.value = null // Reset ao operador
            isNextOperand.value = false // Reset ao estado do próximo operando
        }
    }
}

/**
 * Função auxiliar que realiza os cálculos com base no operador fornecido.
 * @param firstOperand Primeiro operando.
 * @param secondOperand Segundo operando.
 * @param operator Operador matemático (+, -, x, ÷).
 * @return O resultado do cálculo.
 */
private fun calculateResult(firstOperand: Int, secondOperand: Int, operator: String): Int {
    return when (operator) {
        "+" -> firstOperand + secondOperand
        "-" -> firstOperand - secondOperand
        "x" -> firstOperand * secondOperand
        "÷" -> if (secondOperand == 0) 0 else firstOperand / secondOperand // Evitar divisão por zero
        else -> 0
    }
}
