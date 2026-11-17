package ernxe.dev.movieskmp.feature.playground.presentation

import ernxe.dev.movieskmp.MainDispatcherRule
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import ernxe.dev.movieskmp.fakes.FakeMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PlaygroundViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieRepository: FakeMovieRepository
    private lateinit var viewModel: PlaygroundViewModel

    @Before
    fun setup() {
        movieRepository = FakeMovieRepository()
        viewModel = PlaygroundViewModel(movieRepository)
    }

    @Test
    fun `state is initially loading`() = runTest {
        assertEquals(PlaygroundScreenState.Loading, viewModel.state.value.screenState)
        assertTrue(viewModel.state.value.nowPlayingMovies.isEmpty())
    }

    @Test
    fun `movies are loaded successfully`() = runTest {
        val movie = Movie(
            id = 1,
            title = "Movie 1",
            posterPath = null,
            releaseDate = "2026-01-01",
            voteAverage = 8.0,
            popularity = 100.0,
        )
        movieRepository.nowPlayingMoviesApiResult = Result.Success(listOf(movie))

        // WhileSubscribed needs an active collector to start loading.
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.state.collect()
        }

        val state = viewModel.state.first { it.screenState == PlaygroundScreenState.Content }

        assertEquals(PlaygroundScreenState.Content, state.screenState)
        assertEquals(listOf(movie), state.nowPlayingMovies)
    }

    @Test
    fun `empty movies shows content with an empty list`() = runTest {
        movieRepository.nowPlayingMoviesApiResult = Result.Success(emptyList())
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.state.collect()
        }

        val state = viewModel.state.first { it.screenState == PlaygroundScreenState.Content }

        assertEquals(PlaygroundScreenState.Content, state.screenState)
        assertTrue(state.nowPlayingMovies.isEmpty())
    }

    @Test
    fun `movies error shows error state`() = runTest {
        movieRepository.nowPlayingMoviesApiResult = Result.Error(DataError.Remote.NO_INTERNET)
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.state.collect()
        }

        val state = viewModel.state.first { it.screenState is PlaygroundScreenState.Error }

        assertEquals(
            PlaygroundScreenState.Error(DataError.Remote.NO_INTERNET),
            state.screenState,
        )
        assertTrue(state.nowPlayingMovies.isEmpty())
    }
}
