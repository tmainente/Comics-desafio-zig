package com.example.comics.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.comics.data.entities.MovieEntity
import com.example.comics.data.local.dao.MovieDao


@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
@TypeConverters(AppTypeConvertor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}