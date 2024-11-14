package com.example.student

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val studentDao = StudentDatabase.getDatabase(application).studentDao()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> get() = _students

    init {
        loadStudents()
    }

    private fun loadStudents() {
        viewModelScope.launch {
            _students.value = studentDao.getAllStudents()
        }
    }

    // Função para adicionar um novo estudante na base de dados
    fun addStudent(name: String, studentNumber: String, birthDate: String) {
        val student = Student(
            id = 0,
            nome = name,
            numAluno = studentNumber,
            dataNasc = birthDate
        )
        insertStudent(student)
    }

    // Função de inserção direta
    private fun insertStudent(student: Student) {
        viewModelScope.launch {
            studentDao.insert(student)
            loadStudents() // Recarrega a lista após a inserção
        }
    }
}
