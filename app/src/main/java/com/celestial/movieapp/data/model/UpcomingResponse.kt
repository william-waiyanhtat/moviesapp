package com.celestial.movieapp.data.model

import com.google.gson.annotations.SerializedName


data class UpcomingResponse (
   val commonResponse: CommonResponse,

   @SerializedName("result")
   val result: List<MovieModel>,

   @SerializedName("dates")
   val dates: Dates
   )


data class Dates(
   val maximum: String,
   val minimum: String
)