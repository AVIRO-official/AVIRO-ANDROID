package com.android.aviro.data.datasource.marker

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import org.json.JSONArray
import java.io.*
import javax.inject.Inject

class MarkerCacheDataSourceImp @Inject constructor(
    @ApplicationContext private val context: Context
): MarkerCacheDataSource {

    private val PREF_NAME = "CustomMarkerPrefs"
    private val KEY_CUSTOM_DATA = "CustomMarkerList"

    val gson = GsonBuilder().create()

    override fun saveMarker(custom_marker : List<MarkerEntity>) {

        val prefs: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        Log.d("custom_marker","${custom_marker}")
        val list = MarkerListEntity(custom_marker)

        val json = Json { ignoreUnknownKeys = true }
        //val jsonString = gson.toJson(list) //json.encodeToString(list)
/*
        val marker_json_array = JsonArray()
        custom_marker.forEach {
            val marker_json_obj = gson.toJson(it, MarkerEntity::class.java)
            //marker_json_array.put(marker_json_obj)
            marker_json_array.add(marker_json_obj)
        }

        //val jsonArray = gson.toJsonTree(custom_marker, object : TypeToken<List<MarkerEntity>>() {}.type).asJsonArray
        Log.d("marker_json_array","${marker_json_array}")

 */
        editor.putString(KEY_CUSTOM_DATA, list.toString())
        editor.apply()

    }

    override fun getMarker() : MarkerListEntity {

        val prefs: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_CUSTOM_DATA, null)

        //val list = arrayListOf<MarkerListEntity>()

        val jsonArray = gson.fromJson(jsonString, MarkerListEntity::class.java)
        //val json = Json { ignoreUnknownKeys = true } // ignoreUnknownKeys를 사용하여 알 수 없는 속성을 무시
        //val markerListEntity  = json.decodeFromString(MarkerListEntity.,jsonString!!)
        Log.d("jsonArray","${jsonArray}")


        /*

        if (jsonArray != null) {
            jsonArray.map {
                custom_marker_list.add(Gson().fromJson(it, MarkerEntity::class.java))
            }
        } else {
            return emptyList()
        }
        Log.d("custom_marker_list","${custom_marker_list}")

         */

        return jsonArray

    }

    override fun updateMarker() {

    }
}