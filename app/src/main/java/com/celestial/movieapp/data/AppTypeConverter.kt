package com.celestial.movieapp.data
import androidx.room.TypeConverters;
import androidx.room.TypeConverter;
import java.util.*
import kotlin.collections.ArrayList


class AppTypeConverter {

    @TypeConverter
    fun numArrayToString(numArray: Array<Int?>?): String {
        numArray?.let{

            val builder = StringBuilder()
            for(i in it){
                builder.append(i).append(",")
            }
            return@numArrayToString builder.toString()
        }
        return ""
    }

    @TypeConverter
    fun numStringToIntArray(st: String?): Array<Int?>?{
        st?.let {
            var numList = ArrayList<Int>()
            var stArray = st.split(",")

            for (s in stArray){
                numList.add(s.toInt())
            }

            return@numStringToIntArray numList.toTypedArray()
        }
        return null
    }



}