package ernxe.dev.movieskmp.feature.playground.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import ernxe.dev.movieskmp.core.domain.movie.model.Movie
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaygroundScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun topBar_hidesWhenScrolledDown_andReturnsAtTop() {
        // Arrange: show enough sample movies to make the list scrollable.
        val movies = List(100) { index ->
            Movie(
                id = index,
                title = "Movie $index",
                posterPath = null,
                releaseDate = null,
                voteAverage = 0.0,
                popularity = 0.0,
            )
        }
        composeRule.setContent {
            MaterialTheme {
                PlaygroundScreen(
                    state = PlaygroundState(
                        screenState = PlaygroundScreenState.Content,
                        nowPlayingMovies = movies,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        // Assert: the top bar starts visible.
        composeRule.onNodeWithText("Playground").assertIsDisplayed()

        // Act and assert: scroll past the first movie to hide the top bar.
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(20)
        composeRule.onNodeWithText("Playground").assertDoesNotExist()

        // Scroll back to the first movie to show the top bar again.
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(0)
        composeRule.onNodeWithText("Playground").assertIsDisplayed()
    }
}
