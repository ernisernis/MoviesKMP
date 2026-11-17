package ernxe.dev.movieskmp.core.domain.movie.repository

import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getNowPlayingMovies(): Flow<List<Movie>>
    fun getPopularMovies(): Flow<List<Movie>>
    suspend fun getNowPlayingMoviesApi(): Result<List<Movie>, DataError.Remote>
}
