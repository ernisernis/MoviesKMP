package ernxe.dev.movieskmp.core.domain.movie.repository

import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getNowPlayingMovies(): Flow<List<Movie>>
    fun getPopularMovies(): Flow<List<Movie>>
}
