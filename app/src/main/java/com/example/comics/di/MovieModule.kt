package com.example.comics.di

import androidx.room.Room
import com.example.comics.data.local.AppDatabase
import com.example.comics.data.remote.api.Api
import com.example.comics.data.remote.repository.MovieRepository
import com.example.comics.data.remote.repository.MovieRepositoryImpl
import com.example.comics.domain.usecase.GetMovieUseCase
import com.example.comics.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object MovieModule {

    val modules = module {
        single<Api> { get<Retrofit>().create(Api::class.java) }
        single<AppDatabase> {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "movies_database.db"
            )
                .build()
        }

        factory<MovieRepository> {
            MovieRepositoryImpl(
                movieApi = get(),
                appDatabase = get()
            )
        }

        factory { GetMovieUseCase(movieRepository = get()) }

        viewModel {
            MainViewModel(
                getMovieUseCase = get()
            )
        }
    }
}