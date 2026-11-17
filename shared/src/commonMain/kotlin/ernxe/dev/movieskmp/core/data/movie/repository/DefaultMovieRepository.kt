package ernxe.dev.movieskmp.core.data.movie.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import ernxe.dev.movieskmp.core.data.movie.local.MovieDao
import ernxe.dev.movieskmp.core.data.movie.mapper.toMovie
import ernxe.dev.movieskmp.core.data.movie.remote.RemoteMovieDataSource
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.domain.movie.repository.MovieRepository
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.core.result.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
@Inject
class DefaultMovieRepository(
    private val movieDao: MovieDao,
    private val remoteMovieDataSource: RemoteMovieDataSource,
) : MovieRepository {
    override fun getNowPlayingMovies(): Flow<List<Movie>> =
        movieDao.observeNowPlayingMovies()
            .map { movies -> movies.map { it.toMovie() } }

    override fun getPopularMovies(): Flow<List<Movie>> =
        movieDao.observePopularMovies()
            .map { movies -> movies.map { it.toMovie() } }

    override suspend fun getNowPlayingMoviesApi(): Result<List<Movie>, DataError.Remote> =
        remoteMovieDataSource.getNowPlayingMovies()
            .map { response -> response.results.map { it.toMovie() } }
}