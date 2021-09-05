package com.celestial.movieapp.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "remote_keys")
data class CommonResponse (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_result")
    val totalResult: Int,

    var isUpComing: Boolean

){
    @Ignore
    @SerializedName("results")
    val result: List<MovieModel>? = null

}