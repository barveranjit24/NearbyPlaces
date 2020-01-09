package com.example.nearbyplaces.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.nearbyplaces.database.RestaurantsDatabase
import com.example.nearbyplaces.model.MyPlaces
import com.example.nearbyplaces.model.Restaurants
import com.example.nearbyplaces.remote.RestApi
import com.example.nearbyplaces.remote.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MapActivityViewModel (application: Application): BaseViewModel(application) {

    val restaurantsList = MutableLiveData<List<Restaurants>>()
    val restaurantsListLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    val jsonApi: RestApi = RetrofitClient.instance
    private val myCompositeDisposable = CompositeDisposable()

    fun fetchData(url:String) {
        loading.value = true
        myCompositeDisposable?.add(jsonApi.getNearbyPlaces(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({myPlaces -> displayData(myPlaces)} ,
                { t: Throwable -> Log.e("RxError : ","RxError : " + t.message)
                    restaurantsListLoadError.value = true
                    loading.value = false
                })
        )
    }

    private fun displayData(myPlaces: MyPlaces){

        if (myPlaces.status.equals("OK")){
            val restaurants: Array<Restaurants>? = myPlaces.results
            restaurantsList.value = restaurants!!.toList()
            restaurantsListLoadError.value = false
            loading.value = false
            storeRestaurantsLocally(restaurants!!.toList())

        }else{
            restaurantsListLoadError.value = true
            loading.value = false
            fetchFromDatabase()
        }
}

    private fun storeRestaurantsLocally(list: List<Restaurants>) {
        launch {
            val dao = RestaurantsDatabase(getApplication()).restaurantsDao()
            dao.deleteAllRestaurants()
            val result = dao.insertAll(list)
            restaurantsList.value = list
            restaurantsListLoadError.value = false
            loading.value = false
        }
    }

     fun fetchFromDatabase() {
        loading.value = true
        launch {
            val list = RestaurantsDatabase(getApplication()).restaurantsDao().getAllRestaurants()
            restaurantsList.value = list
            restaurantsListLoadError.value = false
            loading.value = false

            Toast.makeText(getApplication(), "Loading offline data", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (myCompositeDisposable!=null){
            myCompositeDisposable.clear()
        }
    }

}