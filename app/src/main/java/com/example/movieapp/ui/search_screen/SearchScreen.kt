package com.example.movieapp.ui.search_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.GlidePreloadingData
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.ui.components.HEADER_HEIGHT
import com.example.movieapp.ui.components.SearchHeader
import com.example.movieapp.ui.main_activity.SnackbarAction
import com.example.movieapp.ui.components.MovieGrid
import com.example.movieapp.ui.main_screen.StatusbarBackground
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    showSnackbar: (String, SnackbarAction) -> Unit,
    navigateToDetails: (Movie) -> Unit,
    navigateUp: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val searchedMovies by viewModel.searchedMovies.collectAsState()
    val currentPage by viewModel.searchPage.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val maxPage by viewModel.maxPage.collectAsState()
    val gridState = rememberLazyGridState()


    val preloadSearchMovies = rememberGlidePreloadingData(
        searchedMovies,
        Size(250f, 500f)
    ) { item, requestBuilder ->
        requestBuilder.load(item.image)
    }

    SearchScreenContent(
        title = "Searched movie",
        movies = preloadSearchMovies,
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
        backBtnOnClick = navigateUp,
        searchText = searchText,
        searchTextChange = { viewModel.setSearchText(it) },
        maxPage = maxPage,
    )
    LaunchedEffect(currentPage, searchText) {
        if (searchText.isEmpty()) viewModel.fetchTopRatedMovies(currentPage) { errorMessage ->
            showSnackbar(
                errorMessage,
                SnackbarAction.Error
            )
        }
        else viewModel.fetchSearchedMovies(
            searchText,
            currentPage,
        ) { errorMessage -> showSnackbar(errorMessage, SnackbarAction.Error) }
    }
    LaunchedEffect(searchText) {
        val job = launch { gridState.animateScrollToItem(0) }
        job.join()
        viewModel.setCurrentPage(1)
    }
    BackHandler(currentPage > 1) {
        scope.launch {
            val job = launch { gridState.animateScrollToItem(0) }
            job.join()
            viewModel.setCurrentPage(currentPage - 1)
        }
    }
}


@Composable
fun SearchScreenContent(
    title: String,
    movies: GlidePreloadingData<Movie>,
    gridState: LazyGridState,
    failedPosterResponse: (String) -> Unit,
    currentPage: Int,
    changePage: (Int) -> Unit,
    movieCardOnClick: (Movie) -> Unit,
    backBtnOnClick: () -> Unit,
    searchText: String,
    searchTextChange: (String) -> Unit,
    maxPage: Int,
) {
    val localFocusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            },
    ) {
        if (movies.size > 0)
            MovieGrid(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = HEADER_HEIGHT.dp),
                gridState = gridState,
                title = title,
                movies = movies,
                failedPosterResponse = failedPosterResponse,
                movieCardOnClick = movieCardOnClick,
                currentPage = currentPage,
                changePage = changePage,
                maxPage = maxPage,
            )
        else EmptySearch()
        AnimatedVisibility(visible = true,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }) {
            SearchHeader(
                text = searchText,
                textChange = searchTextChange,
                modifier = Modifier.statusBarsPadding(),
                backOnClick = backBtnOnClick,
            )
        }
        StatusbarBackground()
    }
}

@Composable
fun EmptySearch() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Empty", style = MaterialTheme.typography.displaySmall)
    }
}
