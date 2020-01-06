package com.example.nearbyplaces.model

import androidx.room.Embedded

class Viewport {

    @Embedded
    var northeast:Northeast?=null

    @Embedded
    var southwest:Southwest?=null

}
