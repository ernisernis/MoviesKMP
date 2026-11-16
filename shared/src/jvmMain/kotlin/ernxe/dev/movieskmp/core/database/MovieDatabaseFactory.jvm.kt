package ernxe.dev.movieskmp.core.database

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun createMovieDatabase(): MovieDatabase {
    val directory = File(System.getProperty("user.home"), ".movieskmp")
    check(directory.isDirectory || directory.mkdirs()) {
        "Could not create the movie database directory: ${directory.absolutePath}"
    }
    val builder = Room.databaseBuilder<MovieDatabase>(
        name = File(directory, MovieDatabase.DATABASE_NAME).absolutePath,
        factory = { MovieDatabaseConstructor.initialize() },
    )
    return buildMovieDatabase(builder, BundledSQLiteDriver())
}
