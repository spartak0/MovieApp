package com.example.movieapp.data.network.api

import com.example.movieapp.data.network.dto.ResponseResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<ResponseResult>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): Response<ResponseResult>

    @GET("search/movie")
    suspend fun getSearchedMovies(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<ResponseResult>
}