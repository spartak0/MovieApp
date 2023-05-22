package com.example.movieapp.di

import com.example.movieapp.data.network.api.RetrofitApi
import com.example.movieapp.data.network.reposiory.NetworkRepositoryImpl
import com.example.movieapp.domain.mapper.MovieMapper
import com.example.movieapp.domain.mapper.ResponseMapper
import com.example.movieapp.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideResponseMapper(movieMapper: MovieMapper): ResponseMapper {
        return ResponseMapper(movieMapper)
    }

    @Provides
    @Singleton
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper()
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        retrofitApi: RetrofitApi,
        responseMapper: ResponseMapper
    ): NetworkRepository {
        return NetworkRepositoryImpl(retrofitApi = retrofitApi, responseMapper = responseMapper)
    }
}