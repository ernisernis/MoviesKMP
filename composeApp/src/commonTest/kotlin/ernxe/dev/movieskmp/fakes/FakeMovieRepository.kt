package ernxe.dev.movieskmp.fakes

import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.domain.movie.repository.MovieRepository
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeMovieRepository : MovieRepository {
    val nowPlayingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies = MutableStateFlow<List<Movie>>(emptyList())

    var nowPlayingMoviesApiResult: Result<List<Movie>, DataError.Remote> =
        Result.Success(emptyList())

    override fun getNowPlayingMovies(): Flow<List<Movie>> = nowPlayingMovies.asStateFlow()

    override fun getPopularMovies(): Flow<List<Movie>> = popularMovies.asStateFlow()

    override suspend fun getNowPlayingMoviesApi(): Result<List<Movie>, DataError.Remote> =
        nowPlayingMoviesApiResult
}
