package ernxe.dev.movieskmp.core.database

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun createMovieDatabase(context: Context): MovieDatabase {
    val builder = Room.databaseBuilder<MovieDatabase>(
        context = context.applicationContext,
        name = MovieDatabase.DATABASE_NAME,
        factory = { MovieDatabaseConstructor.initialize() },
    )
    return buildMovieDatabase(builder, BundledSQLiteDriver())
}
