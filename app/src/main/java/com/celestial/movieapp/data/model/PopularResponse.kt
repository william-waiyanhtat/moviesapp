package com.celestial.movieapp.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body


data class PopularResponse (

  @SerializedName("results")
  val result: List<MovieModel>
  )