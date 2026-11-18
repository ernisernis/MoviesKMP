package ernxe.dev.movieskmp.feature.playground.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.domain.movie.repository.MovieRepository
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.core.result.onError
import ernxe.dev.movieskmp.core.result.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlaygroundState(
    val screenState: PlaygroundScreenState = PlaygroundScreenState.Loading,
    val nowPlayingMovies: List<Movie> = emptyList(),
)

// Could also be PlaygroundLoadingState
// Could also be sealed class, both work
sealed interface PlaygroundScreenState {
    data object Content : PlaygroundScreenState
    data class Error(val error: DataError) : PlaygroundScreenState
    data object Loading : PlaygroundScreenState
}

sealed interface PlaygroundAction {
    data object ButtonClicked : PlaygroundAction
}

sealed interface PlaygroundEvent {
    data object InvokeBackClick : PlaygroundEvent
}

@ViewModelKey(PlaygroundViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class PlaygroundViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PlaygroundState())
    val state = _state
        .onStart {
            loadData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlaygroundState()
        )

    private val eventChannel = Channel<PlaygroundEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private fun loadData() {
        viewModelScope.launch {
            movieRepository
                .getNowPlayingMoviesApi()
                .onSuccess { movies ->
                    _state.update { it.copy(
                        screenState = PlaygroundScreenState.Content,
                        nowPlayingMovies = movies,
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(
                        screenState = PlaygroundScreenState.Error(error)
                    ) }
                }
        }
    }

    fun onAction(action: PlaygroundAction) {
        when (action) {
            PlaygroundAction.ButtonClicked -> TODO()
        }
    }
}
