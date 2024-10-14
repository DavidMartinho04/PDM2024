package com.example.calculadora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorLayout(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalculatorDisplay()
        Spacer(modifier = Modifier.height(35.dp))
        RowButtonRow(listOf("MRC", "M-", "M+", "ON"), redButton = 3)
        RowButtonRow(listOf("√", "%", "+/-", "C"), redButton = 3)
        RowButtonRow(listOf("7", "8", "9", "÷"))
        RowButtonRow(listOf("4", "5", "6", "x"))
        RowButtonRow(listOf("1", "2", "3", "-"))
        RowButtonRow(listOf("0", ".", "=", "+"))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculatorLayout() {
    CalculatorLayout()
}