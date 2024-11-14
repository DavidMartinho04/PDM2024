package com.example.student.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.student.Student

@Composable
fun StudentItem(student: Student) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nome: ${student.nome}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Número: ${student.numAluno}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Data de Nascimento: ${student.dataNasc}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

