package com.example.movieapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlidePreloadingData
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.ui.main_screen.EmptyMovieCard
import com.example.movieapp.ui.main_screen.MovieCard
import com.example.movieapp.ui.theme.spacing

@Composable
fun MovieGrid(
    modifier: Modifier,
    gridState: LazyGridState,
    title: String,
    movies: GlidePreloadingData<Movie>,
    failedPosterResponse: (String) -> Unit,
    movieCardOnClick: (Movie) -> Unit,
    currentPage: Int,
    changePage: (Int) -> Unit,
    maxPage: Int,
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(140.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        contentPadding = PaddingValues(spacing.large),
        userScrollEnabled = movies.size > 0,
        modifier = modifier
    ) {
        itemTitle(title)
        itemMoviesGrid(movies, failedPosterResponse, movieCardOnClick)
        itemPageIndicator(currentPage, maxPage, changePage)
    }
}

@Composable
fun EmptyMovieGrid(
    modifier: Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    title: String,
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(140.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        contentPadding = PaddingValues(spacing.large),
        userScrollEnabled = true,
        modifier = modifier,
    ) {
        itemTitle(title)
        itemEmptyGrid()
        itemSpacer(32.dp)
    }
}

fun LazyGridScope.itemSpacer(large: Dp) {
    item(span = { GridItemSpan(this.maxLineSpan) }) {
        Spacer(modifier = Modifier.height(large))
    }
}

fun LazyGridScope.itemPageIndicator(currentPage: Int, maxPage: Int, changePage: (Int) -> Unit) {
    item(span = { GridItemSpan(this.maxLineSpan) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.large, top = spacing.medium),
        ) {
            PageIndicator(
                modifier = Modifier.align(Alignment.Center),
                currentPage = currentPage,
                onClickBack = { changePage(currentPage - 1) },
                onClickForward = { changePage(currentPage + 1) },
                maxPage = maxPage
            )
        }
    }
}

fun LazyGridScope.itemTitle(title: String) {
    item(span = { GridItemSpan(this.maxLineSpan) }) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

fun LazyGridScope.itemMoviesGrid(
    movies: GlidePreloadingData<Movie>,
    failedPosterResponse: (String) -> Unit,
    movieCardOnClick: (Movie) -> Unit
) {
    items(movies.size) { index ->
        val (movie, preloadRequest) = movies[index]
        MovieCard(
            width = 0.dp,
            height = 250.dp,
            name = movie.title ?: "",
            year = movie.releaseDate?.let { if (it.length > 3) it.substring(0..3) else "" } ?: "",
            imageUrl = movie.image ?: "",
            preloadRequest = preloadRequest,
            onError = failedPosterResponse,
            onClick = { movieCardOnClick(movie) }
        )
    }
}

fun LazyGridScope.itemEmptyGrid() {
    items(12) {
        EmptyMovieCard(width = 0.dp, height = 250.dp)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PageIndicator(
    modifier: Modifier,
    currentPage: Int,
    maxPage: Int,
    onClickBack: () -> Unit,
    onClickForward: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val enabledBackBtn = currentPage > 1
    val enabledForward = currentPage < maxPage
    Box(modifier = modifier.width(120.dp)) {
        IconButton(
            onClick = onClickBack,
            modifier = Modifier.align(Alignment.CenterStart),
            enabled = enabledBackBtn
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                tint = if (enabledBackBtn) color else Color.Transparent,
                contentDescription = null
            )
        }
        AnimatedContent(targetState = currentPage, modifier = Modifier.align(Alignment.Center)) {
            Text(text = it.toString(), color = color)
        }
        IconButton(onClick = onClickForward, modifier = Modifier.align(Alignment.CenterEnd)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                tint = if (enabledForward) color else Color.Transparent,
                contentDescription = null
            )
        }
    }
}

fun Modifier.shimmerEffect(backgroundColor: Color): Modifier = composed {
    val shimmerColors = listOf(
        backgroundColor.copy(alpha = 0.6f),
        backgroundColor.copy(alpha = if (isSystemInDarkTheme()) 0.4f else 0.2f),
        backgroundColor.copy(alpha = 0.6f),
    )
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

