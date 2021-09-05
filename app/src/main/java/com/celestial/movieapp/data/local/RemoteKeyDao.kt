package com.celestial.movieapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.celestial.movieapp.data.model.CommonResponse

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(commonResponse: CommonResponse)

    @Query("SELECT * FROM remote_keys where isUpComing like 1 ORDER BY id DESC")
    suspend fun getUpcomingRemoteKeyByPage(): List<CommonResponse>

    @Query("SELECT * FROM remote_keys where isUpComing like 0 ORDER BY id DESC")
    suspend fun getPopularRemoteKeyByPage(): List<CommonResponse>


    @Query("DELETE FROM remote_keys")
    suspend fun deleteByKey()
}
