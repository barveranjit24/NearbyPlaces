package com.example.nearbyplaces.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.nearbyplaces.R
import com.example.nearbyplaces.model.PhotoList
import com.example.nearbyplaces.util.Util.Companion.getPhotoUrl


class PhotosAdapter(internal var context: Context, internal var photoList: List<PhotoList>)
    : RecyclerView.Adapter<PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photos,parent,false)
        return PhotosViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {

        val photoAttribute = photoList.get(position)

        var photo_reference:String= photoAttribute.photo_reference.toString()
        var height:Int=photoAttribute.height
        var width:Int=photoAttribute.width

        val photoUrl:String = getPhotoUrl(width, height, photo_reference)

        holder.progressBar.setVisibility(View.VISIBLE)
        Glide.with(context)
            .load(photoUrl)
            .placeholder(R.drawable.ic_restaurant) //placeholder
            .error(R.drawable.ic_restaurant) //error
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.setVisibility(View.GONE)
                    return false // important to return false so the error placeholder can be placed
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.setVisibility(View.GONE)
                    return false
                }
            })
            .into(holder.ivPhoto)

    }
}

