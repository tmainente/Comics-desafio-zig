package com.example.comics.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("movie")
data class MovieEntity(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey val id: Long,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    val overview: String,

    @ColumnInfo(name = "image")
    @SerializedName("image")
    val image: String,

    )
