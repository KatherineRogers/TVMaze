package com.example.tvmazesample

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class SharedViewModel : ViewModel() {

    val onNowList = MutableLiveData<ArrayList<ShowMain>>()
    val selectedShow = MutableLiveData<ShowMain>()
    val dateSelected = MutableLiveData<Date>()
    val searchValue = MutableLiveData<String>()
    val episode = MutableLiveData<ShowMain>()

    fun setShow(s: ShowMain){
        selectedShow.value = s
    }

    fun setDate(d: Date){
        dateSelected.value = d
    }

    fun setSearch(s: String){
        searchValue.value = s
    }

    fun fetch(url: String, client: OkHttpClient) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
            }
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                parser(responseData.toString())
            }
        })
    }

    fun parser(json: String){
        val gson = Gson()
        var list: ArrayList<ShowMain>? = null
        var ep: ShowMain? = null
        try {
            val itemType = object : TypeToken<ArrayList<ShowMain>>() {}.type
            list = gson.fromJson<ArrayList<ShowMain>>(json, itemType)
        }catch (e: Exception){
            Log.e("ERROR", e.message.toString())
        }

        try {
            val itemType = object : TypeToken<ShowMain>() {}.type
            ep = gson.fromJson<ShowMain>(json, itemType)
        }catch (e: Exception){
            Log.e("ERROR", e.message.toString())
        }

        if(ep != null){
            episode.postValue(ep!!)
            ep.name?.let { Log.i("EPISODE", it) }
        }else if(list != null) {
            onNowList.postValue(list!!)
        }

    }
}