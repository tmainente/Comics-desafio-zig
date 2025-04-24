package com.example.comics.data.remote.model


import com.example.comics.data.entities.MovieEntity
import com.google.gson.annotations.SerializedName


data class MovieResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<MediaResult>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int


)

data class MediaResult(
    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("media_type")
    val mediaType: String,

    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int
)

private const val URL_IMG = "https://image.tmdb.org/t/p/w500/"

val List<MediaResult>.asMovie: List<MovieEntity>
    get() = map {
        MovieEntity(
            id = it.id,
            image = "${URL_IMG}${it.posterPath}",
            title = it.title,
            overview = it.overview
        )
    }