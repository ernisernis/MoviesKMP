package ernxe.dev.movieskmp.core.data.movie.remote

import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.core.data.movie.remote.dto.MovieResponseDto

interface RemoteMovieDataSource {
    suspend fun getNowPlayingMovies(): Result<MovieResponseDto, DataError.Remote>
}
