package com.example.student

import StudentListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student.ui.AddStudentScreen
import com.example.student.ui.theme.StudentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentTheme {
                var showAddStudentScreen by remember { mutableStateOf(false) }
                val studentViewModel: StudentViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showAddStudentScreen) {
                        AddStudentScreen(
                            studentViewModel = studentViewModel,
                            onSave = { showAddStudentScreen = false },
                            onCancel = { showAddStudentScreen = false }
                        )
                    } else {
                        StudentListScreen(
                            studentViewModel = studentViewModel,
                            onAddStudentClick = { showAddStudentScreen = true }
                        )
                    }
                }
            }
        }
    }
}
