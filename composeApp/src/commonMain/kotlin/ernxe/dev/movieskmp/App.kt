package ernxe.dev.movieskmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ernxe.dev.movieskmp.navigation.Home
import ernxe.dev.movieskmp.navigation.Navigator
import ernxe.dev.movieskmp.navigation.Playground
import ernxe.dev.movieskmp.navigation.Profile
import ernxe.dev.movieskmp.navigation.TOP_LEVEL_ROUTES
import ernxe.dev.movieskmp.navigation.config
import ernxe.dev.movieskmp.navigation.rememberNavigationState
import org.jetbrains.compose.resources.painterResource

import movieskmp.composeapp.generated.resources.Res
import movieskmp.composeapp.generated.resources.compose_multiplatform


@Composable
fun App() {
    val navigationState = rememberNavigationState(
        startRoute = Home,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        homeSection(onSubRouteClick = {})
        profileSection(onSubRouteClick = {})
        playgroundSection(onSubRouteClick = {})
    }

    MaterialTheme {
        Scaffold(bottomBar = {
            NavigationBar {
                TOP_LEVEL_ROUTES.forEach { (key, value) ->
                    val isSelected = key == navigationState.topLevelRoute
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { navigator.navigate(key) },
                        icon = {
                            Icon(
                                painter = painterResource(value.icon),
                                contentDescription = value.description
                            )
                        },
                        label = { Text(value.description) }
                    )

                }
            }
        }) {
            NavDisplay(
                entries = navigationState.toDecoratedEntries(entryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }
}

fun EntryProviderScope<NavKey>.homeSection(
    onSubRouteClick: () -> Unit,
) {
    entry<Home> {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Welcome to nav3, Home screen",
            )
        }
    }
}

fun EntryProviderScope<NavKey>.profileSection(
    onSubRouteClick: () -> Unit,
) {
    entry<Profile> {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Welcome to nav3, Profile screen",
            )
        }
    }
}

fun EntryProviderScope<NavKey>.playgroundSection(
    onSubRouteClick: () -> Unit,
) {
    entry<Playground> {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Welcome to nav3, Playground screen",
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun AppPreview() {
    MaterialTheme {
        App()
    }
}