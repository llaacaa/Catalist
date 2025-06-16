package com.example.catalist.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.data.result.QuizResult
import com.example.catalist.domain.CatRepository
import com.example.catalist.domain.LeaderBoardRepository
import com.example.catalist.domain.LoginRepository
import com.example.catalist.domain.ResultRepository
import com.example.catalist.presentation.screens.details.Image
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val QUESTIONS_COUNT = 20

class QuizViewModel(
    private val catRepository: CatRepository,
    private val loginRepository: LoginRepository,
    private val resultRepository: ResultRepository,
    private val leaderBoardRepository: LeaderBoardRepository

) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private var currentQuestionNumber = 0
    private var questions: List<QuizQuestion> = emptyList()

    private var numberOfCorrectQuestions = 0

    private val _effect = Channel<QuizEvent>()
    val effect = _effect.receiveAsFlow()


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
            if (time < 0) {
                break
            }
        }
    }

    fun onAction(action: QuizAction) {
        when (action) {
            is QuizAction.OnDialogDismiss -> {
                if (!_state.value.terminateQuizCheck) {
                    // nije prekinio

                    viewModelScope.launch {
                        val userData = loginRepository.getUserData().first()
                        println(userData)


                        val timeLeftInSeconds = _state.value.timer.split(":").let { parts ->
                            val minutes = parts[0].toIntOrNull() ?: 0
                            val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
                            minutes * 60 + seconds
                        }

                        val points: Double = _state.value.numberOfCorrectQuestions * 2.5 *
                                (1 + (timeLeftInSeconds + 120) / 300.0)
                        resultRepository.saveQuizResult(QuizResult(
                            points,
                            System.currentTimeMillis(),
                            null
                        ))
                    }



                }


                viewModelScope.launch {
                    _effect.send(QuizEvent.OnQuizCompleted)
                }
            }

            is QuizAction.OnOptionClicked -> {
                val currentState = _state.value
                val currentQuestion = currentState.currentQuestion
                val selectedOption = action.option
                val isCorrect = selectedOption == currentQuestion.correctAnswer

                val updatedCorrectCount = if (isCorrect) {
                    currentState.numberOfCorrectQuestions + 1
                } else {
                    currentState.numberOfCorrectQuestions
                }

                val isLastQuestion = currentState.currentQuestionNumber + 1 >= questions.size

                if (isLastQuestion) {
                    _state.update {
                        it.copy(
                            numberOfCorrectQuestions = updatedCorrectCount,
                            showResultDialog = true
                        )
                    }
                } else {
                    val nextQuestionNumber = currentState.currentQuestionNumber + 1
                    _state.update {
                        it.copy(
                            numberOfCorrectQuestions = updatedCorrectCount,
                            currentQuestionNumber = nextQuestionNumber,
                            currentQuestion = questions[nextQuestionNumber]
                        )
                    }
                }
            }


            is QuizAction.OnQuizQuitClick -> {
                _state.update { it.copy(terminateQuizCheck = true) }
            }

            is QuizAction.OnContinueQuiz -> {
                _state.update { it.copy(terminateQuizCheck = false) }
            }

            is QuizAction.OnTimeRunOut -> {
                //Ipak necu iskoristiti jer tamjer zaustavljam nakom 5 minuta i cekiram da li je tajmer == 0:00
            }

            is QuizAction.OnShareResultClick -> {
                _state.update { it.copy(isShareEnabled = false) }

                viewModelScope.launch {
                    val userData = loginRepository.getUserData().first()
                    println(userData)


                    val timeLeftInSeconds = _state.value.timer.split(":").let { parts ->
                        val minutes = parts[0].toIntOrNull() ?: 0
                        val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
                        minutes * 60 + seconds
                    }

                    val points: Double = _state.value.numberOfCorrectQuestions * 2.5 *
                            (1 + (timeLeftInSeconds + 120) / 300.0)


                    val resultResponse = leaderBoardRepository.sendResult(userData.nickname, points).getOrNull()

                    if (resultResponse == null) {
//                        _effect.send(QuizEvent.OnError("Failed to send result"))
                        return@launch
                    }

                    resultRepository.saveQuizResult(QuizResult(
                        resultResponse.result.result,
                        resultResponse.result.createdAt,
                        resultResponse.ranking
                    ))

                    _effect.send(
                        QuizEvent.OnNavigateToLeaderboard
                    )
                }

            }
        }
    }

}