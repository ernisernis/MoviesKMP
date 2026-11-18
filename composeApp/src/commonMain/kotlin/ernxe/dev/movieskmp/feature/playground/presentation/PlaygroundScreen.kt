package ernxe.dev.movieskmp.feature.playground.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun PlaygroundScreenRoot(
    viewModel: PlaygroundViewModel = metroViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PlaygroundEvent.InvokeBackClick -> TODO()
            }
        }
    }

    AnimatedContent(
        targetState = state.screenState
    ) { targetState ->
        when (targetState) {
            is PlaygroundScreenState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Unable to load movies.")
                }
            }
            PlaygroundScreenState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            PlaygroundScreenState.Content -> {
                PlaygroundScreen(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(
    state: PlaygroundState,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val showTopBar by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Column(modifier = modifier) {
        if (showTopBar) {
            TopAppBar(title = { Text("Playground") })
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
        ) {
            items(state.nowPlayingMovies, key = { it.id }) { movie ->
                Text(
                    text = movie.title,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

/*


    private val query = MutableStateFlow("")
    private val _searchState: StateFlow<ShopItemState> = query
        .debounce(500.milliseconds)
        .transformLatest { query ->
            if (query.isBlank()) {
                emit(ShopItemState.Blank)
                return@transformLatest
            }

            emit(ShopItemState.Loading)

            eshopRepository.searchEshopDevices(name = query)
                .onSuccess { devices ->
                    if (devices.isEmpty()) {
                        emit(ShopItemState.Empty)
                    } else {
                        emit(ShopItemState.Content(devices.map { it.toShopCategoryUi() }))
                    }
                }
                .onError { error ->
                    emit(ShopItemState.Error(error.toUiText()))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ShopItemState.Blank,
        )

           private val _state = MutableStateFlow(ShopState())
    val state = combine(
        _state,
        _searchState,
    ) { state, searchState ->
        state.copy(
            screenState = state.screenState,
            searchText = state.searchText,
            categoryItems = state.categoryItems,
            eshopItemsState = searchState,
        )
    }
        .onStart {
            loadData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ShopState(),
        )


 */