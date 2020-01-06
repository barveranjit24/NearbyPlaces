package com.example.nearbyplaces.model

import androidx.room.Embedded
import androidx.room.Ignore

class Geometry {

    @Embedded
    var location:Location?=null
    @Ignore
    var viewport:Viewport?=null

}
