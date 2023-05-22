package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.NetworkResult
import com.example.movieapp.domain.model.ResponseGeneral
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun getPopularMovie(page: Int): Flow<NetworkResult<ResponseGeneral>>
    suspend fun getSearchedMovie(query: String, page: Int): Flow<NetworkResult<ResponseGeneral>>
    suspend fun getTopRatedMovies(page: Int): Flow<NetworkResult<ResponseGeneral>>
}