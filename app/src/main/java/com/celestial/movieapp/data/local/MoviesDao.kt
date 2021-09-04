package com.celestial.movieapp.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.celestial.movieapp.data.model.MovieModel

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieModel)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAllMovies(movies: List<MovieModel>)

    @Delete
    suspend fun deleteMovie(movie: MovieModel)

    @Query("DELETE FROM movies ")
    suspend fun deleteAllMovies()

    @Query("SELECT * FROM movies")
    fun readAllUpcomingMovies(): PagingSource<Int, MovieModel>

    @Query("SELECT * FROM movies")
    fun readAllPopularMovies(): PagingSource<Int, MovieModel>

    @Update
    suspend fun updateMovie(movie: MovieModel): Int

}