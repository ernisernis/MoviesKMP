package ernxe.dev.movieskmp.core.database

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.async.executeSQL

internal val MIGRATION_2_3 = object : Migration(2, 3) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.executeSQL(
            """
            CREATE TABLE IF NOT EXISTS `now_playing_movies` (
                `id` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `posterPath` TEXT,
                `releaseDate` TEXT,
                `voteAverage` REAL NOT NULL,
                `popularity` REAL NOT NULL,
                `position` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent(),
        )
        connection.executeSQL(
            """
            CREATE TABLE IF NOT EXISTS `popular_movies` (
                `id` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `posterPath` TEXT,
                `releaseDate` TEXT,
                `voteAverage` REAL NOT NULL,
                `popularity` REAL NOT NULL,
                `position` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent(),
        )
        connection.executeSQL(
            """
            INSERT INTO `now_playing_movies`
                (`id`, `title`, `posterPath`, `releaseDate`, `voteAverage`, `popularity`, `position`)
            SELECT m.`id`, m.`title`, m.`posterPath`, m.`releaseDate`,
                   m.`voteAverage`, m.`popularity`, f.`position`
            FROM `movies` AS m
            INNER JOIN `movie_feeds` AS f ON m.`id` = f.`movieId`
            WHERE f.`feed` = 'now_playing'
            """.trimIndent(),
        )
        connection.executeSQL(
            """
            INSERT INTO `popular_movies`
                (`id`, `title`, `posterPath`, `releaseDate`, `voteAverage`, `popularity`, `position`)
            SELECT m.`id`, m.`title`, m.`posterPath`, m.`releaseDate`,
                   m.`voteAverage`, m.`popularity`, f.`position`
            FROM `movies` AS m
            INNER JOIN `movie_feeds` AS f ON m.`id` = f.`movieId`
            WHERE f.`feed` = 'popular'
            """.trimIndent(),
        )
        connection.executeSQL("DROP TABLE `movie_feeds`")
        connection.executeSQL("DROP TABLE `movies`")
    }
}
