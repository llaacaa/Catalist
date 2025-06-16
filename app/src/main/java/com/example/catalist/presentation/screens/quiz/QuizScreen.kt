package com.example.catalist.presentation.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.example.catalist.R

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    state: QuizState,
    onAction: (QuizAction) -> Unit,
) {

    BackHandler {
        onAction(QuizAction.OnQuizQuitClick)
    }

    if (state.isLoading) {
      Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
      ) {
          CircularProgressIndicator()
      }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            onAction(QuizAction.OnQuizQuitClick)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }

                    Text(text = state.timer, modifier = Modifier.align(Alignment.Center))
                }
            }


            LaunchedEffect(key1 = state.currentQuestion) {

            }
            Crossfade(
                targetState = state.currentQuestion,
                animationSpec = tween(300)
            ) { question ->
                Card(
                    modifier = Modifier
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .crossfade(true)
                            .data(question.catImg.url)
                            .error(R.mipmap.ic_launcher_round)
                            .build(),
                        contentDescription = "Cat Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp, min = 400.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )

                    HorizontalDivider()

                    question.options.forEach {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = {
                                onAction(QuizAction.OnOptionClicked(it))
                            }
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

