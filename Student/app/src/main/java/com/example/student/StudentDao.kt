package com.example.student

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {
    @Insert
    suspend fun insert(student: Student)

    @Query("SELECT * FROM student_table")
    suspend fun getAllStudents(): List<Student>

    @Query("DELETE FROM student_table")
    suspend fun deleteAll()
}
