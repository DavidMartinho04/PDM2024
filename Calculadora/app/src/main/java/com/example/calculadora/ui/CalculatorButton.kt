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

@Composable
fun CalculatorButton(label: String, isRed: Boolean = false) {
    Button(
        onClick = { /* TODO: Implementar lógica de clique */ },
        modifier = Modifier
            .size(90.dp)
            .padding(3.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRed) Color.Red else Color.Black,
            contentColor = Color.White
        )
    ) {
        Text(
            text = label,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
