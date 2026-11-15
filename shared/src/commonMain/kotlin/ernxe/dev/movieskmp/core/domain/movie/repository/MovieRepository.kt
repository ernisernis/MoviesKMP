package ernxe.dev.movieskmp.core.domain.movie.repository

import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result

interface MovieRepository {
    suspend fun getNowPlayingMovies(): Result<List<Movie>, DataError.Remote>
}
