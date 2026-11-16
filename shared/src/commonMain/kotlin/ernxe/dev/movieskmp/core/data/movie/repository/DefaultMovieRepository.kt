package ernxe.dev.movieskmp.core.data.movie.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import ernxe.dev.movieskmp.core.data.movie.mapper.toMovie
import ernxe.dev.movieskmp.core.data.movie.remote.RemoteMovieDataSource
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.domain.movie.repository.MovieRepository
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.core.result.map

@ContributesBinding(AppScope::class)
@Inject
class DefaultMovieRepository(
    private val remoteMovieDataSource: RemoteMovieDataSource,
) : MovieRepository {
    override suspend fun getNowPlayingMovies(): Result<List<Movie>, DataError.Remote> {
        return remoteMovieDataSource
            .getNowPlayingMovies()
            .map { response ->
                response.results.map { it.toMovie() }
            }
    }

    override suspend fun getPopularMovies(): Result<List<Movie>, DataError.Remote> {
        return remoteMovieDataSource
            .getPopularMovies()
            .map { response ->
                response.results.map { it.toMovie() }
            }
    }
}