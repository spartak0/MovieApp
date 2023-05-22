package com.example.movieapp.data.network.dto

import com.google.gson.annotations.SerializedName


data class MovieDto(
    val id: String?,
    val title: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterImage: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val rating:Float?,
)