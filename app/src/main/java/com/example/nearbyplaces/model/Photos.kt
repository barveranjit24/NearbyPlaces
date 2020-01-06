package com.example.nearbyplaces.model

import android.os.Parcel
import android.os.Parcelable


class Photos() : Parcelable {

     var photo_reference:String?=null
     var html_attributions:Array<String>?=null
     var height:Int=0
     var width:Int=0

    constructor(parcel: Parcel) : this() {
        photo_reference = parcel.readString()
        html_attributions = parcel.createStringArray()
        height = parcel.readInt()
        width = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photo_reference)
        parcel.writeStringArray(html_attributions)
        parcel.writeInt(height)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photos> {
        override fun createFromParcel(parcel: Parcel): Photos {
            return Photos(parcel)
        }

        override fun newArray(size: Int): Array<Photos?> {
            return arrayOfNulls(size)
        }
    }

}
