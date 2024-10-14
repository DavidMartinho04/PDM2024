package com.example.calculadora.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowButtonRow(buttonLabels: List<String>, redButton: Int = -1) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for ((index, label) in buttonLabels.withIndex()) {
            CalculatorButton(label = label, isRed = index == redButton)
        }
    }
}
