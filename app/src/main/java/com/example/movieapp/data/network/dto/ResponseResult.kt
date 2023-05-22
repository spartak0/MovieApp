package com.example.movieapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ResponseResult(
    @SerializedName("results")
    val movies: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
)