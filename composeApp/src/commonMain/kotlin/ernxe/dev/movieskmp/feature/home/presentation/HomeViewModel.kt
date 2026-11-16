package ernxe.dev.movieskmp.feature.home.presentation

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.error.DataError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
}
