package com.example.comics.domain.usecase

import com.example.comics.data.remote.repository.MovieRepository
import com.example.comics.util.safeRunDispatcher


class GetMovieUseCase (
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke() =
         movieRepository.getMovie()


}