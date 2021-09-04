package com.celestial.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class CommonResponse (
    @SerializedName("page")
    val page: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_result")
    val totalResult: Int
)