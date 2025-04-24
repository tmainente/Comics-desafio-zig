package com.example.comics.di

import com.example.comics.BuildConfig
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
    private val apiKey = BuildConfig.API_KEY
    private val baseUrl = BuildConfig.BASE_URL

    private fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, SECONDS)
            .readTimeout(30, SECONDS)
            .writeTimeout(30, SECONDS)
            .addInterceptor(headerInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun headerInterceptor() =  Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", apiKey)
        val newRequest = requestBuilder.build()
        chain.proceed(newRequest)
    }
}