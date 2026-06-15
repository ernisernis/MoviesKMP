package ernxe.dev.movieskmp.navigation

import androidx.compose.material3.IconButtonShapes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import movieskmp.composeapp.generated.resources.Res
import movieskmp.composeapp.generated.resources.compose_multiplatform
import movieskmp.composeapp.generated.resources.ic_home
import movieskmp.composeapp.generated.resources.ic_home_png
import movieskmp.composeapp.generated.resources.ic_playground
import movieskmp.composeapp.generated.resources.ic_playground_png
import movieskmp.composeapp.generated.resources.ic_profile
import movieskmp.composeapp.generated.resources.ic_profile_png
import org.jetbrains.compose.resources.DrawableResource

internal val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Home::class, Home.serializer())
            subclass(Profile::class, Profile.serializer())
            subclass(Playground::class, Playground.serializer())
        }
    }
}

internal val TOP_LEVEL_ROUTES = mapOf(
    Home to NavBarItem(icon = Res.drawable.ic_home_png, description = "Home"),
    Profile to NavBarItem(icon = Res.drawable.ic_profile_png, description = "Profile"),
    Playground to NavBarItem(icon = Res.drawable.ic_playground_png, description = "Playground"),
)

data class NavBarItem(
    val icon: DrawableResource,
    val description: String,
)

@Serializable
data object Home : NavKey

@Serializable
data object Profile : NavKey

@Serializable
data object Playground : NavKey
