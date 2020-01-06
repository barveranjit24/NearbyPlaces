package com.example.nearbyplaces.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_photos.view.*

class PhotosViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivPhoto = itemView.ivPhoto
    val progressBar = itemView.progressBar
}