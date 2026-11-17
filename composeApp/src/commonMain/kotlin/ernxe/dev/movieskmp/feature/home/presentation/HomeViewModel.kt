package ernxe.dev.movieskmp.feature.home.presentation

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import ernxe.dev.movieskmp.core.data.movie.repository.DefaultMovieRepository
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.core.result.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

data class HomeState(
    val screenState: HomeScreenState = HomeScreenState.Loading,
    val nowPlayingMovies: List<Movie> = emptyList(),
)

sealed interface HomeScreenState {
    data object Content : HomeScreenState
    data class Error(val error: DataError) : HomeScreenState
    data object Loading : HomeScreenState
}

@ViewModelKey(HomeViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class HomeViewModel(
    private val movieRepository: DefaultMovieRepository,
) : ViewModel() {

    val state2 = movieRepository
        .getPopularMovies()
        .map { movies ->

        }
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
}

private fun homeUiState(
    movieRepository: DefaultMovieRepository,
): Flow<HomeScreenState> {

    val popularMovieStream =
        movieRepository.getPopularMovies()

    val nowPlayingMovies =
        movieRepository.getNowPlayingMovies()

    return combine(popularMovieStream, nowPlayingMovies, ::Pair)
        .asResult { DataError.Local.UNKNOWN }
        .map { result ->
            when (result) {
                is Result.Success -> HomeScreenState.Content
                is Result.Error -> HomeScreenState.Error(result.error)
            }
        }
        .onStart { emit(HomeScreenState.Loading) }
}