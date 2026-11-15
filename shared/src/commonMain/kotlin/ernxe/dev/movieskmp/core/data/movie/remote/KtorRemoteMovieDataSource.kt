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
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class KtorRemoteMovieDataSource(
    private val httpClient: HttpClient,
) : RemoteMovieDataSource {
    override suspend fun getNowPlayingMovies(): Result<MovieResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("https://api.themoviedb.org/3/movie/now_playing") {
                parameter("api_key", BuildKonfig.TMDB_API_KEY)
            }
        }
    }
}