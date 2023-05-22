package com.example.movieapp.ui.main_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.GlidePreloadingData
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.ui.components.EmptyMovieGrid
import com.example.movieapp.ui.components.Header
import com.example.movieapp.ui.components.MovieGrid
import com.example.movieapp.ui.main_activity.SnackbarAction
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    showSnackbar: (String, SnackbarAction) -> Unit,
    navigateToDetails: (Movie) -> Unit,
    navigateToSearch: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val movies by viewModel.movies.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val maxPage by viewModel.maxPage.collectAsState()
    val gridState =
        rememberLazyGridState()
    val preloadMovies =
        rememberGlidePreloadingData(
            movies,
            Size(250f, 500f)
        ) { item, requestBuilder ->
            requestBuilder.load(item.image)
        }

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(isRefreshing,
        {
            viewModel.refresh(page = currentPage) { errorMessage ->
                showSnackbar(
                    errorMessage,
                    SnackbarAction.Error
                )
            }
        })

    MainScreenContent(
        title = "Movies",
        movies = preloadMovies,
        gridState = gridState,
        failedPosterResponse = { errorMessage -> showSnackbar(errorMessage, SnackbarAction.Error) },
        currentPage = currentPage,
        changePage = { newPage ->
            scope.launch {
                val job = launch { gridState.animateScrollToItem(0) }
                job.join()
                viewModel.setCurrentPage(newPage)
            }
        },
        movieCardOnClick = navigateToDetails,
        searchBtnOnClick = navigateToSearch,
        maxPage = maxPage,
        pullRefreshState = pullRefreshState,
        isRefreshing = isRefreshing
    )
    LaunchedEffect(currentPage) {
        viewModel.fetchPopularMovies(
            currentPage
        ) { errorMessage -> showSnackbar(errorMessage, SnackbarAction.Error) }
    }

    BackHandler(currentPage > 1) {
        scope.launch {
            val job = launch { gridState.animateScrollToItem(0) }
            job.join()
            viewModel.setCurrentPage(currentPage - 1)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenContent(
    title: String,
    movies: GlidePreloadingData<Movie>,
    gridState: LazyGridState,
    failedPosterResponse: (String) -> Unit,
    currentPage: Int,
    changePage: (Int) -> Unit,
    movieCardOnClick: (Movie) -> Unit,
    searchBtnOnClick: () -> Unit,
    maxPage: Int,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (movies.size > 0)
            MovieGrid(
                modifier = Modifier.statusBarsPadding(),
                gridState = gridState,
                title = title,
                movies = movies,
                failedPosterResponse = failedPosterResponse,
                movieCardOnClick = movieCardOnClick,
                currentPage = currentPage,
                changePage = changePage,
                maxPage = maxPage
            )
        else EmptyMovieGrid(
            modifier = Modifier.statusBarsPadding(),
            title = title,
        )
        AnimatedVisibility(visible = gridState.canScrollBackward,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }) {
            Header(
                title = title,
                modifier = Modifier.statusBarsPadding(),
                searchBtnOnClick = searchBtnOnClick
            )
        }
        PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        StatusbarBackground()
    }
}

@Composable
fun StatusbarBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(MaterialTheme.colorScheme.primary)
    )
}