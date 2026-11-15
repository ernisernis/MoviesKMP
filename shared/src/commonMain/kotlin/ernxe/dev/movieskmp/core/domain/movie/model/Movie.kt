package ernxe.dev.movieskmp.core.domain.movie.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val popularity: Double,
)