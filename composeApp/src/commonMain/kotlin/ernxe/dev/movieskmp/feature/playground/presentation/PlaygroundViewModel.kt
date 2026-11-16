package ernxe.dev.movieskmp.feature.playground.presentation

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.error.DataError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlaygroundState(
    val screenState: PlaygroundScreenState = PlaygroundScreenState.Loading,
    val popularMovies: List<Movie> = emptyList(),
)

sealed interface PlaygroundScreenState {
    data object Content : PlaygroundScreenState
    data class Error(val error: DataError) : PlaygroundScreenState
    data object Loading : PlaygroundScreenState
}

@ViewModelKey(PlaygroundViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class PlaygroundViewModel : ViewModel() {
    private val _state = MutableStateFlow(PlaygroundState())
    val state = _state.asStateFlow()
}
