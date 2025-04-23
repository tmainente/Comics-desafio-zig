import com.example.comics.data.entities.MovieEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("page")
    val page: Int,

    @SerialName("results")
    val results: List<MediaResult>,

    @SerialName("total_pages")
    val totalPages: Int,

    @SerialName("total_results")
    val totalResults: Int


)

@Serializable
data class MediaResult(
    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("id")
    val id: Long,

    @SerialName("title")
    val title: String,

    @SerialName("original_title")
    val originalTitle: String,

    @SerialName("overview")
    val overview: String,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("media_type")
    val mediaType: String,

    @SerialName("adult")
    val adult: Boolean,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("genre_ids")
    val genreIds: List<Int>,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("video")
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int
)

val List<MediaResult>.asMovie: List<MovieEntity>
    get() = map {
        MovieEntity(
            id = it.id,
            image = "https://image.tmdb.org/t/p/w500/${it.backdropPath}",
            title = it.title,
            overview = it.overview
        )
    }