package com.example.nearbyplaces

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nearbyplaces.adapter.PhotosAdapter
import com.example.nearbyplaces.model.Photos
import com.example.nearbyplaces.model.Restaurants
import com.example.nearbyplaces.util.Constants.Companion.INTENT_PARAM_PHOTOS
import kotlinx.android.synthetic.main.activity_photos_list.*


class PhotosListActivity : AppCompatActivity() {

    var photoList: List<Photos> = ArrayList<Photos>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_list)

        val extras = intent.extras
        if (extras != null) {
            var restaurant: Restaurants = extras.getSerializable(INTENT_PARAM_PHOTOS) as Restaurants
            photoList = restaurant.photos?.toList() ?: photoList
        }
            initView()
    }

    private fun initView() {
        //===========View======================
        if (photoList!=null && photoList!!.size>0){
            photoError.visibility = View.GONE
            recycler_photos.visibility = View.VISIBLE
            recycler_photos.setHasFixedSize(true)
            recycler_photos.layoutManager = GridLayoutManager(this,2)

            val adapter = PhotosAdapter(this,photoList)
            recycler_photos.adapter = adapter
        }else{
            photoError.visibility = View.VISIBLE
            recycler_photos.visibility = View.GONE
        }

        ivBack.setOnClickListener { finish() }
    }
}