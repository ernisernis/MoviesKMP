package ernxe.dev.movieskmp

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json

fun createTmdbClient(
    accessToken: String
): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }

        defaultRequest {
            url("https://api.themoviedb.org/3/")
            bearerAuth(accessToken)
            accept(ContentType.Application.Json)
        }
    }
}