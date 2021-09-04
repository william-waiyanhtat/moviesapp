package com.celestial.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.celestial.movieapp.data.model.CommonResponse
import com.celestial.movieapp.data.model.MovieModel

@Database(
    entities = [MovieModel::class, CommonResponse::class],
    version = 1
)

abstract class MoviesDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    abstract fun remoteKeyDao(): RemoteKeyDao

}