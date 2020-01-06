package com.example.nearbyplaces.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.nearbyplaces.util.Constants.Companion.PHOTOS_BASE_URL
import java.lang.StringBuilder

class Util {

    companion object{
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getUrl(latitude: Double, longitude: Double): String {
            val googlePlaceUrl = StringBuilder("maps/api/place/nearbysearch/json")
                .append("?location=$latitude,$longitude")
                .append("&radius=2000")
                .append("&type=restaurant")
                .append("&key=AIzaSyBHRZoFAHUe_dkkhrclrS5FyH3jAsFgCAk")

            return googlePlaceUrl.toString()
        }

        fun getPhotoUrl(width:Int, height:Int, photo_reference:String): String {
            val googlePhotoUrl = StringBuilder(PHOTOS_BASE_URL)
                .append("?maxwidth=250")
                .append("&maxheight=250")
                .append("&photoreference=$photo_reference")
                .append("&key=AIzaSyBHRZoFAHUe_dkkhrclrS5FyH3jAsFgCAk")

            return googlePhotoUrl.toString()
        }
    }
}