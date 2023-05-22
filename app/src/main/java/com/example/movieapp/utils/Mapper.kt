package com.example.movieapp.utils

interface Mapper<Dto, Domain> {
    fun dtoToDomain(dto:Dto): Domain
    fun domainToDto(domain: Domain): Dto
}