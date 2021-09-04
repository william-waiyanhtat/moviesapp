package com.celestial.movieapp.data.model

import com.google.gson.annotations.SerializedName


data class PopularResponse (

  val commonResponse: CommonResponse,

  @SerializedName("results")
  val result: MovieModel
  )