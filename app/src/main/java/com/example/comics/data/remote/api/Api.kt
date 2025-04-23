package com.example.comics.data.remote.api

import MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("3/trending/movie/day")
    suspend fun getAllMovie(): Response<MovieResponse>
}