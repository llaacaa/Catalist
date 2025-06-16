package com.example.catalist.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catalist.presentation.screens.details.DetailsActions
import com.example.catalist.presentation.screens.details.DetailsScreen
import com.example.catalist.presentation.screens.details.DetailsViewModel
import com.example.catalist.presentation.screens.gallery.GalleryScreen
import com.example.catalist.presentation.screens.gallery.GalleryViewModel
import com.example.catalist.presentation.screens.leader_board.LeaderBoardScreen
import com.example.catalist.presentation.screens.leader_board.LeaderBoardViewModel
import com.example.catalist.presentation.screens.list.ListAction
import com.example.catalist.presentation.screens.list.ListScreen
import com.example.catalist.presentation.screens.list.ListViewModel
import com.example.catalist.presentation.screens.login.LoginEvent
import com.example.catalist.presentation.screens.login.LoginScreen
import com.example.catalist.presentation.screens.login.LoginViewModel
import com.example.catalist.presentation.screens.quiz.QuizScreen
import com.example.catalist.presentation.screens.quiz.QuizViewModel
import com.example.catalist.presentation.theme.CatalistTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalistTheme {


                val navController = rememberNavController()

                val mainViewModel = koinViewModel<MainViewModel>()

                if (mainViewModel.state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Surface {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                        ) { padding ->
                            NavHost(
                                navController = navController,
                                modifier = Modifier.padding(padding),
                                startDestination = if (mainViewModel.state.isLoggedIn) Route.Home else Route.Login,
                            ) {

                                composable<Route.Login>(
                                    enterTransition = {
                                        fadeIn(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideIntoContainer(
                                            animationSpec = tween(300, easing = EaseIn),
                                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                                        )
                                    },
                                    exitTransition = {
                                        fadeOut(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideOutOfContainer(
                                            animationSpec = tween(300, easing = EaseOut),
                                            towards = AnimatedContentTransitionScope.SlideDirection.End
                                        )
                                    }
                                ) {
                                    val viewModel: LoginViewModel = koinViewModel()
                                    val state by viewModel.state.collectAsState()
                                    val context = LocalContext.current

                                    LaunchedEffect(key1 = Unit) {
                                        viewModel.events.collectLatest { event ->
                                            when (event) {
                                                is LoginEvent.OnSuccessfulLogin -> {
                                                    navController.navigate(Route.Home) {
                                                        popUpTo(Route.Login) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }

                                                is LoginEvent.Error -> {
                                                    Toast.makeText(
                                                        context,
                                                        event.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                                    LoginScreen(
                                        modifier = Modifier.fillMaxSize(),
                                        state = state,
                                        onLoginAction = viewModel::onAction,
                                    )

                                }

                                composable<Route.Home>(
                                    enterTransition = {
                                        fadeIn(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideIntoContainer(
                                            animationSpec = tween(300, easing = EaseIn),
                                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                                        )
                                    },
                                    exitTransition = {
                                        fadeOut(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideOutOfContainer(
                                            animationSpec = tween(300, easing = EaseOut),
                                            towards = AnimatedContentTransitionScope.SlideDirection.End
                                        )
                                    }
                                ) {

                                    val viewmodel: ListViewModel = koinViewModel()
                                    val state by viewmodel.state.collectAsState()
                                    val context = LocalContext.current

                                    LaunchedEffect(key1 = Unit) {
                                        viewmodel.errorChannel.collect {
                                            Toast.makeText(
                                                context,
                                                "Oops! An Unknown Error Occured",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    ListScreen(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        state = state,
                                        onSearchQueryChanged = {
                                            viewmodel.onSearchQueryChanged(it)
                                        },
                                        onItemClicked = { id ->
                                            navController.navigate(
                                                Route.Details(
                                                    id = id
                                                )
                                            )
                                        },
                                        onAction = { action ->
                                            when (action) {
                                                is ListAction.OnQuizClick -> {
                                                    navController.navigate(Route.Quiz)
                                                }

                                                is ListAction.OnUserProfileClick -> {
                                                    navController.navigate(Route.Profile)
                                                }
                                            }
                                        }
                                    )
                                }

                                composable<Route.Details>(
                                    enterTransition = {
                                        fadeIn(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideIntoContainer(
                                            animationSpec = tween(300, easing = EaseIn),
                                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                                        )
                                    },
                                    exitTransition = {
                                        fadeOut(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideOutOfContainer(
                                            animationSpec = tween(300, easing = EaseOut),
                                            towards = AnimatedContentTransitionScope.SlideDirection.End
                                        )
                                    }
                                ) {
                                    val viewModel: DetailsViewModel = koinViewModel()

                                    LaunchedEffect(key1 = null) {
                                        viewModel.errorChannel.collect {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Oops! An Unknown Error Occured",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    val state by viewModel.state.collectAsState()

                                    DetailsScreen(
                                        state = state,
                                        onWikipediaClick = {
                                            if (it.isBlank()) {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "No Wikipedia link available",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@DetailsScreen
                                            }
                                            val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                                            startActivity(intent)
                                        },
                                        onAction = { action ->
                                            when (action) {
                                                is DetailsActions.OnGalleryClick -> {

                                                    navController.navigate(Route.Gallery(action.id))
                                                }
                                            }
                                        }
                                    )
                                }

                                composable<Route.Gallery>(
                                    enterTransition = {
                                        fadeIn(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideIntoContainer(
                                            animationSpec = tween(300, easing = EaseIn),
                                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                                        )
                                    },
                                    exitTransition = {
                                        fadeOut(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideOutOfContainer(
                                            animationSpec = tween(300, easing = EaseOut),
                                            towards = AnimatedContentTransitionScope.SlideDirection.End
                                        )
                                    }
                                ) {

                                    val viewModel: GalleryViewModel = koinViewModel()
                                    val state by viewModel.state.collectAsState()
                                    GalleryScreen(
                                        state = state,
                                        onAction = viewModel::onAction
                                    )
                                }

                                composable<Route.LeaderBoard> {
                                    val viewModel: LeaderBoardViewModel = koinViewModel()
                                    val state by viewModel.state.collectAsState()
                                    LeaderBoardScreen(
                                        state = state,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }

                                composable<Route.Quiz>(
                                    enterTransition = {
                                        fadeIn(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideIntoContainer(
                                            animationSpec = tween(300, easing = EaseIn),
                                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                                        )
                                    },
                                    exitTransition = {
                                        fadeOut(
                                            animationSpec = tween(
                                                300, easing = LinearEasing
                                            )
                                        ) + slideOutOfContainer(
                                            animationSpec = tween(300, easing = EaseOut),
                                            towards = AnimatedContentTransitionScope.SlideDirection.End
                                        )
                                    }
                                ) {
                                    val viewModel: QuizViewModel = koinViewModel()
                                    val state by viewModel.state.collectAsState()

                                    QuizScreen(
                                        state = state,
                                        onAction = viewModel::onAction,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Profile  : Route

    @Serializable
    data class Details(
        val id: String,
    ) : Route

    @Serializable
    data object Login : Route

    @Serializable
    data class Gallery(
        val id: String,
    ) : Route

    @Serializable
    data object LeaderBoard : Route

    @Serializable
    data object Quiz : Route

}