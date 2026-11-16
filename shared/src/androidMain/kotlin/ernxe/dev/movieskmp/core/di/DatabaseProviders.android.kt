package ernxe.dev.movieskmp.core.di

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import ernxe.dev.movieskmp.core.data.movie.local.MovieDao
import ernxe.dev.movieskmp.core.database.MovieDatabase
import ernxe.dev.movieskmp.core.database.createMovieDatabase

@ContributesTo(AppScope::class)
interface DatabaseProviders {
    @Provides
    @SingleIn(AppScope::class)
    fun provideMovieDatabase(context: Context): MovieDatabase = createMovieDatabase(context)

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()
}
