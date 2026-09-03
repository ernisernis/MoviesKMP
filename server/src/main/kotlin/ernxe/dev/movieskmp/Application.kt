package ernxe.dev.movieskmp

import ernxe.dev.movieskmp.client.createTmdbClient
import ernxe.dev.movieskmp.model.tmdb.TmdbPopularMoviesResponse
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(plugin = ContentNegotiation) {
        json()
    }

    // Need TMDB_ACCESS_TOKEN in .env root project file
    val dotenv = dotenv()
    val tmdbToken = dotenv["TMDB_ACCESS_TOKEN"]
        ?: error("TMDB_ACCESS_TOKEN is missing")

    val tmdbClient = createTmdbClient(tmdbToken)

    routing {
        get("/") {
            call.respondText("Server running")
        }

        get("/movies/popular") {
            val response = tmdbClient
                .get("movie/popular")
                .body<TmdbPopularMoviesResponse>()

            call.respond(response)
        }
    }
}