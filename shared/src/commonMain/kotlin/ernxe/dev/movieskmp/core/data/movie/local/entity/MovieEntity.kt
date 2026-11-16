package ernxe.dev.movieskmp.core.data.movie.local.entity

data class MovieEntity(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val popularity: Double,
)
