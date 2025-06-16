package com.example.catalist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY createdAt DESC")
    suspend fun getAll(): List<QuizResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResultEntity)
}