package com.example.nearbyplaces.model

import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurants")
class Restaurants {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "r_id")
    var r_id:Int=0

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name:String?=""

    @ColumnInfo(name = "icon")
    @SerializedName("icon")
    var icon:String?=""

    @Embedded
    var geometry:Geometry?=null

    @Ignore
    val photos:Array<Photos>?=null

    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id:String?=""

    @ColumnInfo(name = "place_id")
    @SerializedName("place_id")
    var place_id:String?=""

    @ColumnInfo(name = "price_level")
    @SerializedName("price_level")
    var price_level:Int=0

    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    var rating:Double=0.0

    @ColumnInfo(name = "reference")
    @SerializedName("reference")
    var reference:String?=""

    @ColumnInfo(name = "scope")
    @SerializedName("scope")
    var scope:String?=""

    @ColumnInfo(name = "vicinity")
    @SerializedName("vicinity")
    var vicinity:String?=""

    @Embedded
    var opening_hours:OpeningHours? = null

    @Ignore
    @ColumnInfo(name = "types")
    @SerializedName("types")
    var types:Array<String>?=null

}
