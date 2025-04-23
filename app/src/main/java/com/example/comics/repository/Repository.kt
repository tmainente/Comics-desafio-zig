package com.example.comics.repository

import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class Repository {

    suspend fun getComics() = Retrofit.Builder()
        .baseUrl("https://gateway.marvel.com/v1/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(Api::class.java).getComics(
            apikey = "b7e14bab409c70a5c4e7c2b319c09d7b",
            ts = "1682982412",
            hash = "3482f01e9bf207a437a4b621c91339ad"
        ).await()

}