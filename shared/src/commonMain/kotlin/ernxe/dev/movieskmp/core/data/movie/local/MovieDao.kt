package ernxe.dev.movieskmp.core.data.movie.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import ernxe.dev.movieskmp.core.data.movie.local.entity.NowPlayingMovieEntity
import ernxe.dev.movieskmp.core.data.movie.local.entity.PopularMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM now_playing_movies ORDER BY position")
    fun observeNowPlayingMovies(): Flow<List<NowPlayingMovieEntity>>

    @Upsert
    suspend fun upsertNowPlayingMovies(movies: List<NowPlayingMovieEntity>)

    @Query("DELETE FROM now_playing_movies WHERE id NOT IN (:movieIds)")
    suspend fun deleteOtherNowPlayingMovies(movieIds: List<Int>)

    @Query("DELETE FROM now_playing_movies")
    suspend fun deleteAllNowPlayingMovies()

    @Transaction
    suspend fun replaceNowPlayingMovies(movies: List<NowPlayingMovieEntity>) {
        val uniqueMovies = movies.distinctBy { it.movie.id }
        if (uniqueMovies.isEmpty()) {
            deleteAllNowPlayingMovies()
        } else {
            upsertNowPlayingMovies(uniqueMovies)
            deleteOtherNowPlayingMovies(uniqueMovies.map { it.movie.id })
        }
    }

    @Query("SELECT * FROM popular_movies ORDER BY position")
    fun observePopularMovies(): Flow<List<PopularMovieEntity>>

    @Upsert
    suspend fun upsertPopularMovies(movies: List<PopularMovieEntity>)

    @Query("DELETE FROM popular_movies WHERE id NOT IN (:movieIds)")
    suspend fun deleteOtherPopularMovies(movieIds: List<Int>)

    @Query("DELETE FROM popular_movies")
    suspend fun deleteAllPopularMovies()

    @Transaction
    suspend fun replacePopularMovies(movies: List<PopularMovieEntity>) {
        val uniqueMovies = movies.distinctBy { it.movie.id }
        if (uniqueMovies.isEmpty()) {
            deleteAllPopularMovies()
        } else {
            upsertPopularMovies(uniqueMovies)
            deleteOtherPopularMovies(uniqueMovies.map { it.movie.id })
        }
    }
}
