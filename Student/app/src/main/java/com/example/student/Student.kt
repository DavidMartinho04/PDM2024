package com.example.student

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val numAluno: String,
    val nome: String,
    val dataNasc: String
)
