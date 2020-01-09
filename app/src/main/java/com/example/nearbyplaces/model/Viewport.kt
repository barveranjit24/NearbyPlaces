package com.example.nearbyplaces.model

import androidx.room.Embedded
import java.io.Serializable

class Viewport : Serializable {

    @Embedded
    var northeast:Northeast?=null

    @Embedded
    var southwest:Southwest?=null

}
