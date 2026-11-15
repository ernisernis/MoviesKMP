package ernxe.dev.movieskmp.core.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import ernxe.dev.movieskmp.core.network.HttpClientFactory
import io.ktor.client.HttpClient

@ContributesTo(AppScope::class)
interface NetworkProviders {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(): HttpClient = HttpClientFactory.create()
}