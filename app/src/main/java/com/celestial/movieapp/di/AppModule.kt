package com.celestial.movieapp.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.celestial.movieapp.BuildConfig
import com.celestial.movieapp.R
import com.celestial.movieapp.data.MoviesRepository
import com.celestial.movieapp.data.local.MoviesDao
import com.celestial.movieapp.data.local.MoviesDatabase
import com.celestial.movieapp.data.network.api.MoviesAPI
import com.celestial.movieapp.di.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context)= context

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )

    @Singleton
    @Provides
    fun provideMovieAPI(): MoviesAPI{

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()



        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(MoviesAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieDao(
        database: MoviesDatabase
    ) = database.moviesDao()

    @Singleton
    @Provides
    fun provideMoviesDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, MoviesDatabase::class.java, Constants.DB_NAME).build()


    @Singleton
    @Provides
    fun provideMoviesRepository(
        db: MoviesDatabase,
        api: MoviesAPI,
        @ApplicationContext context: Context
    ) = MoviesRepository(db, api, context)





}