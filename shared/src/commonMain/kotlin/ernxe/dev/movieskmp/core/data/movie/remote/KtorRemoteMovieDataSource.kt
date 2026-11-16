package ernxe.dev.movieskmp.core.data.movie.remote

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import ernxe.dev.movieskmp.BuildKonfig
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.network.safeCall
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.core.data.movie.remote.dto.MovieResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

@ContributesBinding(AppScope::class)
@Inject
class KtorRemoteMovieDataSource(
    private val httpClient: HttpClient,
) : RemoteMovieDataSource {
    override suspend fun getNowPlayingMovies(): Result<MovieResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("https://api.themoviedb.org/3/movie/now_playing") {
                bearerAuth(BuildKonfig.TMDB_ACCESS_TOKEN)
            }
        }
    }

    override suspend fun getPopularMovies(): Result<MovieResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("https://api.themoviedb.org/3/movie/popular") {
                bearerAuth(BuildKonfig.TMDB_ACCESS_TOKEN)
            }
        }
    }
}