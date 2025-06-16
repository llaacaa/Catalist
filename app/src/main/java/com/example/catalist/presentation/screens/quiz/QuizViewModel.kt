package com.example.catalist.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.CatRepository
import com.example.catalist.presentation.screens.details.Image
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val QUESTIONS_COUNT = 20

class QuizViewModel(
    private val catRepository: CatRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private var currentQuestionNumber = 0
    private var questions: List<QuizQuestion> = emptyList()

    private val numberOfCorrectQuestions = MutableStateFlow(0)

    init {

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            setupQuiz()
            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            timer().collect {

                if (it <= 0) {
                }
                val minutes = it / 60
                val seconds = if (it % 60 < 10) {
                    "0${it % 60}"
                } else {
                    "${it % 60}"
                }

                _state.update {
                    it.copy(
                        timer = "$minutes:$seconds"
                    )
                }
            }

        }


    }

    private suspend fun setupQuiz() {
        val allCatsResponse = catRepository.getAllBreeds()
        val allCats = allCatsResponse.getOrNull()?.shuffled() ?: return

        val validCatIds = mutableListOf<String>()
        val candidates = allCats.toMutableList()
        while (validCatIds.size < QUESTIONS_COUNT && candidates.isNotEmpty()) {
            val cat = candidates.removeAt(0)
            cat.id?.let { id ->
                val images = catRepository.getCatImagesFor(id).getOrNull()
                if (!images.isNullOrEmpty()) {
                    validCatIds += id
                }
            }
        }

        val catIdsToCatImages = validCatIds.associateWith { catId ->
            val imgs = catRepository.getCatImagesFor(catId).getOrNull()!!
            val img = imgs.random()
            Image(url = img.url, aspectRatio = img.width.toFloat() / img.height.toFloat())
        }


        val allPossibleCatTemperaments = allCats.mapNotNull {
            it.temperament?.split(", ")
        }.flatten().toSet().toList()

        val quizQuestions = buildList {

            catIdsToCatImages.forEach { (catId, catImg) ->

                val bool = Random.nextBoolean()
                val cat = allCats.firstOrNull { it.id == catId } ?: return

                // pogodi rasu
                if (bool) {
                    val correctRace = cat.name ?: return
                    val incorrectGuesses =
                        allCats.shuffled().takeWhile { it.id != catId }.take(3).mapNotNull {
                            it.name
                        }
                    add(
                        QuizQuestion(
                            type = QuestionType.GUESS_THE_RACE,
                            catImg = catImg,
                            options = (incorrectGuesses + correctRace).shuffled(),
                            correctAnswer = correctRace,
                        )
                    )
                } else {
                    val correctTemperaments =
                        cat.temperament?.split(", ")?.shuffled() ?: emptyList()

                    val incorrectTemperaments = allPossibleCatTemperaments.mapNotNull {
                        if (it in correctTemperaments) null
                        else it
                    }.shuffled().take(3)

                    val correctAnswer = correctTemperaments.firstOrNull() ?: ""
                    add(
                        QuizQuestion(
                            type = QuestionType.ODD_ONE_OUT,
                            catImg = catImg,
                            options = (incorrectTemperaments + correctAnswer).shuffled(),
                            correctAnswer = correctAnswer,
                        )
                    )

                }
            }
        }

        this.questions = quizQuestions
        val currentQuestion = quizQuestions.firstOrNull()

        _state.update {
            it.copy(
                currentQuestionNumber = currentQuestionNumber,
                currentQuestion = currentQuestion ?: QuizQuestion(),
            ).also {
                println("aloooooooo $it")
            }
        }

    }

    private fun timer(): Flow<Int> = flow {
        var time = 5 * 60
        while (true) {
            emit(time--)
            delay(1000L)
        }
    }

    fun onAction(action: QuizAction) {
        when (action) {
            QuizAction.OnDialogDismiss -> {

            }

            is QuizAction.OnOptionClicked -> {
                currentQuestionNumber++
                _state.update { it.copy(
                    currentQuestion = questions[currentQuestionNumber],
                    currentQuestionNumber = currentQuestionNumber
                ) }

                val selectedOption = action.option

                val current = _state.value.currentQuestion
                val isCorrect = selectedOption == current.correctAnswer

                if (isCorrect) {
                    numberOfCorrectQuestions.update { it + 1 }
                }
            }

            QuizAction.OnQuizQuitClick -> {

            }

            QuizAction.OnTimeRunOut -> {

            }
        }
    }

}