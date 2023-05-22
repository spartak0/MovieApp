package com.example.movieapp.ui.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.NetworkResult
import com.example.movieapp.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {
    private val _searchedMovies = MutableStateFlow<List<Movie>>(listOf())
    val searchedMovies = _searchedMovies.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val searchPage = _currentPage.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _maxPage = MutableStateFlow(Int.MAX_VALUE)
    val maxPage = _maxPage

    fun fetchSearchedMovies(query: String, page: Int, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.getSearchedMovie(query, page).collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Success -> {
                        _searchedMovies.value = networkResult.data.movies
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

    fun fetchTopRatedMovies(page: Int, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.getTopRatedMovies(page).collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Success -> {
                        _searchedMovies.value = networkResult.data.movies
                    }

                    is NetworkResult.Error -> {
                        networkResult.message?.let { onError(it) }
                    }

                    else -> {}
                }
            }
        }
    }

    fun setSearchText(text: String) {
        viewModelScope.launch {
            _searchText.value = text
        }
    }

    fun setCurrentPage(page: Int) {
        viewModelScope.launch {
            _currentPage.value = page
        }
    }
}