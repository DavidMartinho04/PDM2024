package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorLayout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculatorLayout(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalculatorDisplay()
        Spacer(modifier = Modifier.height(24.dp))

        RowButtonRow(listOf("MRC", "M-", "M+", "ON/C"), redButton = 3)
        RowButtonRow(listOf("√", "%", "+/-", "C"), redButton = 3)
        RowButtonRow(listOf("7", "8", "9", "÷"))
        RowButtonRow(listOf("4", "5", "6", "x"))
        RowButtonRow(listOf("1", "2", "3", "-"))
        RowButtonRow(listOf("0", ".", "=", "+"))
    }
}

@Composable
fun CalculatorDisplay() {
    Box(
        modifier = Modifier
            .width(350.dp)
            .height(100.dp)
            .background(Color(193, 230, 193, 170), shape = RoundedCornerShape(16.dp))
            .padding(17.dp),
    ) {
        Text(
            text = "123456",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterEnd) // Alinha o texto à direita
        )
    }
}

@Composable
fun RowButtonRow(buttonLabels: List<String>, redButton: Int = -1) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for ((index, label) in buttonLabels.withIndex()) {
            CalculatorButton(label = label, isRed = index == redButton)
        }
    }
}

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

@Preview(showBackground = true)
@Composable
fun PreviewCalculator() {
    CalculadoraTheme {
        CalculatorLayout()
    }
}
