package com.example.comics.domain.usecase

import com.example.comics.data.remote.repository.MovieRepository

class GetMovieUseCase (
    private val movieRepository: MovieRepository
) {

     operator fun invoke() =
         movieRepository.getMovie()
}