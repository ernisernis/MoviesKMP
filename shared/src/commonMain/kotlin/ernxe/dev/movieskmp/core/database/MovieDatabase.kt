package ernxe.dev.movieskmp.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.sqlite.SQLiteDriver
import ernxe.dev.movieskmp.core.data.movie.local.MovieDao
import ernxe.dev.movieskmp.core.data.movie.local.entity.NowPlayingMovieEntity
import ernxe.dev.movieskmp.core.data.movie.local.entity.PopularMovieEntity

@Database(
    entities = [NowPlayingMovieEntity::class, PopularMovieEntity::class],
    version = 3,
)
@ConstructedBy(MovieDatabaseConstructor::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "movies.db"
    }
}

@Suppress("KotlinNoActualForExpect")
expect object MovieDatabaseConstructor : RoomDatabaseConstructor<MovieDatabase> {
    override fun initialize(): MovieDatabase
}

internal fun buildMovieDatabase(
    builder: RoomDatabase.Builder<MovieDatabase>,
    driver: SQLiteDriver,
): MovieDatabase = builder
    .setDriver(driver)
    .addMigrations(MIGRATION_2_3)
    .fallbackToDestructiveMigration(dropAllTables = false)
    .build()
