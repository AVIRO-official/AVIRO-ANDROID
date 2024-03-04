package com.android.aviro.data.model.restaurant

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class LocOfRestaurant : RealmObject {
    @PrimaryKey
    var placeId: String = ""
    var x: Double = 0.0
    var y: Double = 0.0
    var allVegan: Boolean = true
    var someMenuVegan: Boolean = false
    var ifRequestVegan: Boolean = false
}