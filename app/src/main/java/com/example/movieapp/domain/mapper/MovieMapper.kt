package com.example.movieapp.domain.mapper

import com.example.movieapp.data.network.dto.MovieDto
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.utils.Constant
import com.example.movieapp.utils.Mapper

class MovieMapper : Mapper<MovieDto, Movie> {
    override fun dtoToDomain(dto: MovieDto): Movie = Movie(
        id = dto.id,
        title = dto.title,
        overview = dto.overview,
        image = Constant.IMAGE_URL_BASE + dto.posterImage,
        releaseDate = dto.releaseDate,
        rating = dto.rating
    )


    override fun domainToDto(domain: Movie): MovieDto = MovieDto(
        id = domain.id,
        title = domain.title,
        overview = domain.overview,
        posterImage = domain.image,
        releaseDate = domain.releaseDate,
        rating = domain.rating,
    )

}