package com.example.catalist.domain

import com.example.catalist.data.result.QuizResult

interface ResultRepository {
    suspend fun getQuizResults(): List<QuizResult>

    suspend fun saveQuizResult(result: QuizResult)
}