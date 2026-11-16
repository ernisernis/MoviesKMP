package ernxe.dev.movieskmp.core.database

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createMovieDatabase(): MovieDatabase {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val path = "${requireNotNull(directory?.path)}/${MovieDatabase.DATABASE_NAME}"
    val builder = Room.databaseBuilder<MovieDatabase>(
        name = path,
        factory = { MovieDatabaseConstructor.initialize() },
    )
    return buildMovieDatabase(builder, BundledSQLiteDriver())
}
