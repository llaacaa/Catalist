package com.example.catalist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CatInfoEntity::class, Image::class, QuizResultEntity::class], version = 1,
    exportSchema = true

)
abstract class CatDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun quizResultDao(): QuizResultDao
}