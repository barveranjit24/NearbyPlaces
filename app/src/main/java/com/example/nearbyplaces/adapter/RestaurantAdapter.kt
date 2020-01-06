package com.example.nearbyplaces.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nearbyplaces.PhotosListActivity
import com.example.nearbyplaces.R
import com.example.nearbyplaces.model.PhotoList
import com.example.nearbyplaces.model.Photos
import com.example.nearbyplaces.model.Restaurants
import com.example.nearbyplaces.util.Constants.Companion.INTENT_PARAM_PHOTOS
import com.example.nearbyplaces.util.Util


class RestaurantAdapter(internal var context: Context, internal var restaurantList:List<Restaurants>)
    : RecyclerView.Adapter<RestaurantViewHolder>() {

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
        var isOpen = restaurant.opening_hours?.open_now ?: false

        holder.txtName.text = restaurant.name.toString()
        holder.txtLocation.text = restaurant.vicinity.toString()
        holder.txtRating.text = restaurant.rating.toString()

        if (restaurant.rating >= 3) {
            holder.txtRating.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_rating_background_green
                )
            )
        } else if (restaurant.rating >= 2) {
            holder.txtRating.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_rating_background_yellow
                )
            )
        } else {
            holder.txtRating.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_rating_background_red
                )
            )
        }

        if (isOpen) {
            holder.txtOpeningHours.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.txtOpeningHours.text = context.getString(R.string.open)
        } else {
            holder.txtOpeningHours.setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.txtOpeningHours.text = context.getString(R.string.closed)
        }

         Glide.with(context)
                .load(restaurant.icon.toString())
                .placeholder(R.drawable.ic_restaurant) //placeholder
                .error(R.drawable.ic_restaurant) //error
                .into(holder.ivIcon);


        holder.ivIcon!!.setOnClickListener {

            if (Util.isOnline(context)){
                var photosList: List<Photos> = restaurant.photos?.toList() ?: emptyList()
                if (photosList==null || photosList.size<=0){
                    Toast.makeText(context,context.getString(R.string.no_photos_found),Toast.LENGTH_SHORT).show()
                }else{
                    var list: ArrayList<PhotoList> = ArrayList<PhotoList>()
                    try{
                            for (i in 0 until photosList.size) {
                                var photoList = PhotoList()
                                photoList.height = photosList[i].height
                                photoList.photo_reference = photosList[i].photo_reference
                                photoList.width = photosList[i].width
                                list.add(photoList)

                            }
                        }catch (e:Exception){
                                e.printStackTrace()
                        }
                    val intent = Intent(context, PhotosListActivity::class.java).apply {
                                                putParcelableArrayListExtra(INTENT_PARAM_PHOTOS,list)

                    }
                    context.startActivity(intent)
                }

            }else{
                Toast.makeText(context,context.getString(R.string.you_are_offline),Toast.LENGTH_SHORT).show()
            }
        }
    }
}