package com.example.nearbyplaces.remote

import com.example.nearbyplaces.model.MyPlaces
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface RestApi {

    @GET
    fun getNearbyPlaces(@Url url: String): Observable<MyPlaces>

}