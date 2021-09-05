package com.celestial.movieapp.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.celestial.movieapp.data.model.MovieModel

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

    @Query("SELECT * FROM movies where isUpcoming = 1 ORDER BY time ASC")
    suspend fun readAllUpcomingMovieList(): List<MovieModel>

    @Query("SELECT * FROM movies where isUpcoming = 0 ORDER BY time ASC")
    suspend fun readAllPopularMovieList(): List<MovieModel>

    @Query("SELECT * FROM movies where page =:page and isUpcoming=1 ORDER BY time ASC")
    suspend fun readUpcomingMovieListByPage(page: Int): List<MovieModel>

    @Query("SELECT * FROM movies where page =:page and isUpcoming=0 ORDER BY time ASC")
    suspend fun readPopularMovieListByPage(page: Int): List<MovieModel>

}