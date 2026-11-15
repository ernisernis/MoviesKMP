package ernxe.dev.movieskmp.feature.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = metroViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.screenState) {
        HomeScreenState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        HomeScreenState.Content -> {
            HomeScreen(
                state = state,
                modifier = Modifier.fillMaxSize(),
            )
        }

        is HomeScreenState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Unable to load movies.")
            }
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(state.nowPlayingMovies, key = { it.id }) { movie ->
            Text(
                text = movie.title,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
