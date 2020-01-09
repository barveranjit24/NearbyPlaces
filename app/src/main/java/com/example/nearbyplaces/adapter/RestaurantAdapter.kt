package com.example.nearbyplaces.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nearbyplaces.PhotosListActivity
import com.example.nearbyplaces.R
import com.example.nearbyplaces.model.Photos
import com.example.nearbyplaces.model.Restaurants
import com.example.nearbyplaces.util.Constants.Companion.INTENT_PARAM_PHOTOS
import com.example.nearbyplaces.util.Util
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantAdapter(internal var context: Context, internal var restaurantList:List<Restaurants>)
    : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant,parent,false)
        return RestaurantViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return restaurantList.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant: Restaurants = restaurantList[position]
        holder.bindPhoto(restaurant)
    }

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var restaurant: Restaurants? = null

        init {
            itemView.ivIcon.setOnClickListener(this)
        }

        fun bindPhoto(restaurant: Restaurants) {
            this.restaurant = restaurant

            var isOpen = restaurant.opening_hours?.open_now ?: false

            itemView.tvName.text = restaurant.name.toString()
            itemView.tvLocation.text = restaurant.vicinity.toString()
            itemView.tvRating.text = restaurant.rating.toString()

            if (restaurant.rating >= 3) {
                itemView.tvRating.setBackground(ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_rating_background_green
                    )
                )
            } else if (restaurant.rating >= 2) {
                itemView.tvRating.setBackground(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_rating_background_yellow
                    )
                )
            } else {
                itemView.tvRating.setBackground(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_rating_background_red
                    )
                )
            }

            if (isOpen) {
                itemView.tvOpeningHour.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                itemView.tvOpeningHour.text = itemView.context.getString(R.string.open)
            } else {
                itemView.tvOpeningHour.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                itemView.tvOpeningHour.text = itemView.context.getString(R.string.closed)
            }

            Glide.with(itemView.context)
                .load(restaurant.icon.toString())
                .placeholder(R.drawable.ic_restaurant) //placeholder
                .error(R.drawable.ic_restaurant) //error
                .into(itemView.ivIcon);
        }

        override fun onClick(v: View?) {
            if (Util.isOnline(itemView.context)){
                val photosList: List<Photos> = restaurant?.photos?.toList() ?: emptyList()
                if (photosList==null || photosList.size<=0){
                    Toast.makeText(itemView.context,itemView.context.getString(R.string.no_photos_found),Toast.LENGTH_SHORT).show()
                }else{
                        val context = itemView.context
                        val showPhotoIntent = Intent(context, PhotosListActivity::class.java)
                        showPhotoIntent.putExtra(INTENT_PARAM_PHOTOS,restaurant)
                        context.startActivity(showPhotoIntent)
                }
            }else{
                Toast.makeText(itemView.context,itemView.context.getString(R.string.you_are_offline),Toast.LENGTH_SHORT).show()
            }
        }

    }

}