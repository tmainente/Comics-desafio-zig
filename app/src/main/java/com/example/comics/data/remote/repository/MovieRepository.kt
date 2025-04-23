package com.example.comics.data.remote.repository

import com.example.comics.data.entities.MovieEntity
import com.example.comics.data.remote.util.Resource
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getMovie(): Flow<Resource<List<MovieEntity>>>
}