package com.example.catalist.presentation.screens.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.example.catalist.R


@Composable
fun GalleryScreen(
    state: GalleryState,
    onAction: (GalleryAction) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
    ) {
        items(state.images) { image ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .crossfade(true)
                    .data(image.url)
                    .error(R.mipmap.ic_launcher_round)
                    .build(),
                contentDescription = "Cat Image",
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onAction(GalleryAction.OnImageClick(image.url))
                    },
                contentScale = ContentScale.Crop,
            )
        }
    }


    val targetColor = if (state.isInPagerViewMode) Color.Black.copy(alpha = 0.4f) else Color.Transparent
    val color by animateColorAsState(targetValue = targetColor, tween(400))

    AnimatedVisibility(
        modifier = Modifier.background(color).imePadding(),
        visible = state.isInPagerViewMode,
        exit = slideOutVertically(tween(300)) { it },
        enter = slideInVertically(tween(300)) { it }
    ) {
        BackHandler {
            onAction(GalleryAction.OnExitPagerViewMode)
        }
        Box(
            Modifier
                .fillMaxSize()
                .clickable {
                    onAction(GalleryAction.OnExitPagerViewMode)
                }
                .padding(vertical = 50.dp)
        ) {

            HorizontalPager(
                modifier = Modifier.matchParentSize().align(Alignment.Center),
                state = rememberPagerState(
                    state.clickedImageIndex,
                    pageCount = { state.images.size })
            ) {
                val image = state.images[it]
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .crossfade(true)
                        .data(image.url)
                        .error(R.mipmap.ic_launcher_round)
                        .build(),
                    contentDescription = "Cat Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}