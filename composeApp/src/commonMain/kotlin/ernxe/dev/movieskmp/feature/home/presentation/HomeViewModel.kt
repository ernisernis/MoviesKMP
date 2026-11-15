package ernxe.dev.movieskmp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.domain.movie.repository.MovieRepository
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.onError
import ernxe.dev.movieskmp.core.result.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val screenState: HomeScreenState = HomeScreenState.Loading,
    val nowPlayingMovies: List<Movie> = emptyList(),
)

sealed interface HomeScreenState {
    data object Content : HomeScreenState
    data class Error(val error: DataError.Remote) : HomeScreenState
    data object Loading : HomeScreenState
}

@ViewModelKey(HomeViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class HomeViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart { initData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeState(),
        )

    private fun initData() {
        _state.update { it.copy(screenState = HomeScreenState.Loading) }
        viewModelScope.launch {
            movieRepository
                .getNowPlayingMovies()
                .onSuccess { movies ->
                    _state.update {
                        it.copy(
                            screenState = HomeScreenState.Content,
                            nowPlayingMovies = movies,
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(screenState = HomeScreenState.Error(error = error))
                    }
                }
        }
    }
}
