package ernxe.dev.movieskmp.core.data.movie.local.entity

import androidx.room3.Embedded
import androidx.room3.Entity

@Entity(tableName = "now_playing_movies", primaryKeys = ["id"])
data class NowPlayingMovieEntity(
    @Embedded val movie: MovieEntity,
    val position: Int,
)

@Entity(tableName = "popular_movies", primaryKeys = ["id"])
data class PopularMovieEntity(
    @Embedded val movie: MovieEntity,
    val position: Int,
)
