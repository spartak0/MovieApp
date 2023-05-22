package com.example.movieapp.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.NetworkResult
import com.example.movieapp.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>>(listOf())
    val movies = _movies.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    private var _maxPage = MutableStateFlow(Int.MAX_VALUE)
    val maxPage = _maxPage.asStateFlow()


    fun fetchPopularMovies(page: Int, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.getPopularMovie(page).collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Success -> {
                        _movies.value = networkResult.data.movies
                        _maxPage.value = networkResult.data.totalPages
                    }

                    is NetworkResult.Error -> {
                        networkResult.message?.let { onError(it) }
                    }

                    else -> {}
                }
            }
        }
    }

    fun setCurrentPage(page: Int) {
        viewModelScope.launch {
            _currentPage.value = page
        }
    }

    fun refresh(page: Int, onError: (String) -> Unit) {
        viewModelScope.launch {
            fetchPopularMovies(page,onError)
        }
    }


}