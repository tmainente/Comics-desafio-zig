package com.example.comics.data.remote.repository


import androidx.room.withTransaction
import asMovie
import com.example.comics.data.local.AppDatabase
import com.example.comics.data.remote.api.Api
import com.example.comics.data.remote.util.networkBoundResource

class MovieRepositoryImpl (
    private val movieApi: Api,
    private val appDatabase: AppDatabase,
) : MovieRepository {

    private val movieDao = appDatabase.movieDao()


    override suspend fun getMovie() = networkBoundResource(
        query = {
            movieDao.getAllMovie()
        },
        fetch = {
            movieApi.getAllMovie()
        },
        saveFetchResult = { movie ->
            appDatabase.withTransaction {
                movieDao.insertAllMovie(movie.body()!!.results.asMovie)
            }
        }
    )


}