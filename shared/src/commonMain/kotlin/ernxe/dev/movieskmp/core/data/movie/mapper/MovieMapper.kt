package ernxe.dev.movieskmp.core.data.movie.mapper

import ernxe.dev.movieskmp.core.data.movie.remote.dto.MovieDto
import ernxe.dev.movieskmp.core.domain.movie.model.Movie

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
    )
}
