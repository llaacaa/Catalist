package com.example.catalist.data.result

import com.example.catalist.data.local.QuizResultDao
import com.example.catalist.data.local.QuizResultEntity
import com.example.catalist.domain.ResultRepository

class ResultRepositoryImpl(
    private val dao: QuizResultDao
) : ResultRepository {

    override suspend fun getQuizResults(): List<QuizResult> =
        dao.getAll().map { entity ->
            QuizResult(
                score = entity.score,
                createdAt = entity.createdAt,
                ranking = entity.ranking
            )
        }

    override suspend fun saveQuizResult(result: QuizResult) {
        dao.insert(
            QuizResultEntity(
                score = result.score,
                createdAt = result.createdAt,
                ranking = result.ranking
            )
        )
    }
}