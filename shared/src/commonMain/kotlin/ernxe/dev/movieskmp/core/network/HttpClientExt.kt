package ernxe.dev.movieskmp.core.network

import ernxe.dev.movieskmp.core.error.DataError
import ernxe.dev.movieskmp.core.result.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()

        return Result.Error(
            when (e) {
                is UnresolvedAddressException -> DataError.Remote.NO_INTERNET
                is SerializationException -> DataError.Remote.SERIALIZATION
                is HttpRequestTimeoutException -> DataError.Remote.REQUEST_TIMEOUT
                else -> DataError.Remote.UNKNOWN
            }
        )
    }

    currentCoroutineContext().ensureActive()
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                Result.Error(DataError.Remote.SERVER_ERROR)
            } catch (_: JsonConvertException) {
                currentCoroutineContext().ensureActive()
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        400 -> Result.Error(DataError.Remote.BAD_REQUEST)
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.SERVER_ERROR)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}
