package com.example.nearbyplaces

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nearbyplaces.adapter.RestaurantAdapter
import com.example.nearbyplaces.util.Constants.Companion.MY_PERMISSION_CODE
import com.example.nearbyplaces.util.Util
import com.example.nearbyplaces.util.Util.Companion.getUrl
import com.example.nearbyplaces.viewmodel.MapActivityViewModel
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private var mMarker: Marker?=null

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private var latitude:Double=0.toDouble()
    private var longitude:Double=0.toDouble()

    private lateinit var viewModel: MapActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProviders.of(this).get(MapActivityViewModel::class.java)

        //===========View======================
        listError.visibility = View.GONE
        recycler_retaurants.setHasFixedSize(true)
        recycler_retaurants.layoutManager = LinearLayoutManager(this)
        //=====================================================

        //Request runtime permission
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkLocationPermission()){
                accessMapLiveLocation()
            }
        }else{
            accessMapLiveLocation()
        }

        observeViewModel()
    }

    private fun accessMapLiveLocation() {
        buildLocationRequest()
        buildLocationCallback()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 500000
        locationRequest.fastestInterval = 300000
        locationRequest.smallestDisplacement = 10f
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0!!.locations.size-1) //Get last location

                if (mMarker!=null){
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude,longitude)
                val  markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.you_are_here))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                mMarker = mMap!!.addMarker(markerOptions)

                //Move Camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))

                val url = getUrl(latitude,longitude)

                if (Util.isOnline(this@MapsActivity)){
                    viewModel.fetchData(url)
                }else{
                    Toast.makeText(this@MapsActivity,this@MapsActivity.getString(R.string.you_are_offline),Toast.LENGTH_SHORT).show()
                    viewModel.fetchFromDatabase()
                }

            }
        }
    }

    fun observeViewModel() {
        viewModel.restaurantsList.observe(this, Observer {restaurantsList ->
            restaurantsList?.let {
                recycler_retaurants.visibility = View.VISIBLE

                val adapter = RestaurantAdapter(this,restaurantsList)
                recycler_retaurants.adapter = adapter

                for (i in 0 until restaurantsList.size)
                {
                    val markerOptions=MarkerOptions()
                    val googlePlace = restaurantsList.get(i)
                    val lat = googlePlace.geometry!!.location!!.lat
                    val lng = googlePlace.geometry!!.location!!.lng
                    val placeName = googlePlace.name
                    val latLng = LatLng(lat,lng)

                    markerOptions.position(latLng)
                    markerOptions.title(placeName)
//                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_marker))
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                    markerOptions.snippet(i.toString())//Assign Index for marker
                    //Add marker to map
                    mMap!!.addMarker(markerOptions)

                }

                //Move Camera
                val latLng = LatLng(latitude,longitude)
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))

                if (restaurantsList.size<=0){
                    listError.text = getString(R.string.restaurants_not_found)
                    listError.visibility = View.VISIBLE
                    loadingView.visibility = View.GONE
                    recycler_retaurants.visibility = View.GONE
                }

            }
        })

        viewModel.restaurantsListLoadError.observe(this, Observer {isError ->
            isError?.let {
                listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {
                    listError.visibility = View.GONE
                    recycler_retaurants.visibility = View.GONE
                }
            }
        })
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION ))
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            return false
        }
        else
            return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            MY_PERMISSION_CODE->{
                if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    if (checkLocationPermission()){

                        buildLocationRequest()
                        buildLocationCallback()

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

                        mMap!!.isMyLocationEnabled=true
                    }
                }
                else{
                    Toast.makeText(this,this.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onStop() {
        try{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }catch (e: Exception){
            e.printStackTrace()
        }
        super.onStop()
    }

}