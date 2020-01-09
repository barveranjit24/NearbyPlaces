package com.example.nearbyplaces.model

import androidx.room.Embedded
import androidx.room.Ignore
import java.io.Serializable

class Geometry : Serializable{

    @Embedded
    var location:Location?=null
    @Ignore
    var viewport:Viewport?=null

}
