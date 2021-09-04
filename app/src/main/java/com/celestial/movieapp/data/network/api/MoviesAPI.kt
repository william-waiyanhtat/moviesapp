package com.celestial.movieapp.data.network.api

import com.celestial.movieapp.data.model.PopularResponse
import com.celestial.movieapp.data.model.UpcomingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {

    @GET("/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String,
        @Query("language")
        lang: String = "en-US",
        @Query("page")
        page: Int
    ): Response<PopularResponse>


    @GET("/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key")
        apiKey: String,
        @Query("language")
        lang: String = "en-US",
        @Query("page")
        page: Int
    ): Response<UpcomingResponse>

}