package ernxe.dev.movieskmp.core.data.movie.mapper

import ernxe.dev.movieskmp.core.data.movie.local.entity.MovieEntity
import ernxe.dev.movieskmp.core.data.movie.local.entity.NowPlayingMovieEntity
import ernxe.dev.movieskmp.core.data.movie.local.entity.PopularMovieEntity
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

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        popularity = popularity,
    )
}

fun NowPlayingMovieEntity.toMovie(): Movie = movie.toMovie()

fun PopularMovieEntity.toMovie(): Movie = movie.toMovie()
