package com.example.nearbyplaces.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_restaurant.view.*


class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtName = itemView.tvName
    val txtLocation = itemView.tvLocation
    val txtOpeningHours = itemView.tvOpeningHour
    val txtRating = itemView.tvRating
    val ivIcon = itemView.ivIcon
}