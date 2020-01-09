package com.example.nearbyplaces.remote

import com.example.nearbyplaces.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var ourInstance: Retrofit?=null

    val instance: RestApi
    get() {
        if (ourInstance ==null){
            ourInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return ourInstance!!.create(RestApi::class.java)
    }
}