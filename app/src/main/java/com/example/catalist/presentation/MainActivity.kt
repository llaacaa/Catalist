package com.example.catalist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    NavHost(
                        navController = rememberNavController(),
                        modifier = Modifier.padding(padding),
                        startDestination = Route.Home
                    ) {

                        composable<Route.Home> {

                            val viewmodel: ListViewModel = koinViewModel()
                            val state by viewmodel.state.collectAsState()
                            ListScreen(
                                state = state
                            )
                        }

                        composable<Route.Details> {

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