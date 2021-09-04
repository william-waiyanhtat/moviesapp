package com.celestial.movieapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "remote_keys")
data class CommonResponse (

    @PrimaryKey(autoGenerate = false)
    val movieType: String = "upcoming",

    @SerializedName("page")
    val page: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_result")
    val totalResult: Int
)