package com.example.catalist.presentation.screens.leader_board

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LeaderBoardScreen(
    state: LeaderBoardState,
    modifier: Modifier = Modifier,
) {
    LazyColumn {
        itemsIndexed(state.items) { index, item ->
            Text(
                text = "${index + 1}. ${item.nickname} — Score: ${item.score} — Quizzes: ${item.quizzesPlayed}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}