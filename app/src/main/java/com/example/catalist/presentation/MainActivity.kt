package com.example.catalist.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catalist.presentation.screens.details.DetailsScreen
import com.example.catalist.presentation.screens.details.DetailsViewModel
import com.example.catalist.presentation.screens.list.ListScreen
import com.example.catalist.presentation.screens.list.ListViewModel
import com.example.catalist.presentation.theme.CatalistTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalistTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) { padding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(padding),
                        startDestination = Route.Home
                    ) {

                        composable<Route.Home> {

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
                                }
                            )
                        }

                        composable<Route.Details> {
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
                            )
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
    data class Details(
        val id: String
    ) : Route

}