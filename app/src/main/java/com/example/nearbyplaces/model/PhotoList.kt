package com.example.nearbyplaces.model

import android.os.Parcel
import android.os.Parcelable

class PhotoList() :Parcelable {

     var photo_reference:String?=null
     var height:Int=0
     var width:Int=0

    constructor(parcel: Parcel) : this() {
        photo_reference = parcel.readString()
        height = parcel.readInt()
        width = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photo_reference)
        parcel.writeInt(height)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoList> {
        override fun createFromParcel(parcel: Parcel): PhotoList {
            return PhotoList(parcel)
        }

        override fun newArray(size: Int): Array<PhotoList?> {
            return arrayOfNulls(size)
        }
    }

}