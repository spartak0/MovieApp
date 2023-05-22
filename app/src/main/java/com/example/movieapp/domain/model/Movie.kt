package com.example.movieapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
    val id: String?,
    val title: String?,
    val overview: String?,
    val image: String?,
    val releaseDate: String?,
    val rating: Float?,
) : Parcelable