package com.example.movieapp.data.network.reposiory

import com.example.movieapp.data.network.api.RetrofitApi
import com.example.movieapp.domain.mapper.ResponseMapper
import com.example.movieapp.domain.model.NetworkResult
import com.example.movieapp.domain.model.ResponseGeneral
import com.example.movieapp.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(
    private val retrofitApi: RetrofitApi,
    private val responseMapper: ResponseMapper,
) : NetworkRepository {
    override suspend fun getPopularMovie(page: Int): Flow<NetworkResult<ResponseGeneral>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = retrofitApi.getPopularMovies(page)
            if (response.isSuccessful)
                response.body()?.let { responseResult ->
                    val mappedResponse = responseMapper.dtoToDomain(responseResult)
                    emit(NetworkResult.Success(mappedResponse))
                }
            else {
                emit(NetworkResult.Error(response.message()))
            }
        } catch (t: Throwable) {
            emit(NetworkResult.Error(t.message))

        }
    }

    override suspend fun getSearchedMovie(
        query: String,
        page: Int
    ): Flow<NetworkResult<ResponseGeneral>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = retrofitApi.getSearchedMovies(page, query)
            if (response.isSuccessful)
                response.body()?.let { responseResult ->
                    val mappedResponse = responseMapper.dtoToDomain(responseResult)
                    emit(NetworkResult.Success(mappedResponse))
                }
            else {
                emit(NetworkResult.Error(response.message()))
            }
        } catch (t: Throwable) {
            emit(NetworkResult.Error(t.message))

        }
    }

    override suspend fun getTopRatedMovies(
        page: Int
    ): Flow<NetworkResult<ResponseGeneral>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = retrofitApi.getTopRatedMovies(page)
            if (response.isSuccessful)
                response.body()?.let { responseResult ->
                    val mappedResponse = responseMapper.dtoToDomain(responseResult)
                    emit(NetworkResult.Success(mappedResponse))
                }
            else {
                emit(NetworkResult.Error(response.message()))
            }
        } catch (t: Throwable) {
            emit(NetworkResult.Error(t.message))

        }
    }
}