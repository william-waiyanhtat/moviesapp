package com.celestial.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.celestial.movieapp.data.AppTypeConverter
import com.celestial.movieapp.data.model.MovieModel

@Database(
    entities = [MovieModel::class],
    version = 1
)

@TypeConverters(AppTypeConverter::class)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

}