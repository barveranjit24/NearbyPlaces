package com.example.nearbyplaces

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nearbyplaces.adapter.PhotosAdapter
import com.example.nearbyplaces.model.PhotoList
import com.example.nearbyplaces.util.Constants
import kotlinx.android.synthetic.main.activity_photos_list.*
import kotlin.collections.ArrayList


class PhotosListActivity : AppCompatActivity() {

    var photoList: List<PhotoList> = ArrayList<PhotoList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_list)

        val extras = intent.extras
        if (extras != null) {
            photoList = extras.getParcelableArrayList<PhotoList>(Constants.INTENT_PARAM_PHOTOS)!!
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
