package com.example.comics.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

object NetworkModule {
    val modules = module {
        single { provideRetrofit() }
    }

    private fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, SECONDS)
            .readTimeout(30, SECONDS)
            .writeTimeout(30, SECONDS)
            .addInterceptor(headerInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun headerInterceptor() =  Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMjJjNmNjZTJmMThjZGQ3YmEwYmUyYjlmYTJiOGU3MyIsIm5iZiI6MTc0MzE3OTQyMi4zODgsInN1YiI6IjY3ZTZjZTllZjg0Njc5NGU5OTEwZjY0MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.i-FWN8afMCXSY6Skjs1K_5pf_qyX8yaU0ehq4ve8SqM")
        val newRequest = requestBuilder.build()
        chain.proceed(newRequest)
    }
}