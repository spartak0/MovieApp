package com.example.movieapp.domain.mapper

import com.example.movieapp.data.network.dto.ResponseResult
import com.example.movieapp.domain.model.ResponseGeneral
import com.example.movieapp.utils.Mapper

class ResponseMapper(val movieMapper: MovieMapper) : Mapper<ResponseResult, ResponseGeneral> {
    override fun dtoToDomain(dto: ResponseResult): ResponseGeneral {
        val movieList = dto.movies.map { movieMapper.dtoToDomain(it) }
        return ResponseGeneral(movieList, dto.totalPages)

    }

    override fun domainToDto(domain: ResponseGeneral): ResponseResult {
        val movieList = domain.movies.map { movieMapper.domainToDto(it) }
        return ResponseResult(movieList, domain.totalPages)
    }
}