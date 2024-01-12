package com.android.aviro.data.datasource.marker

import com.android.aviro.data.entity.marker.MarkerEntity
import com.naver.maps.map.overlay.Marker
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MarkerLocalDataSourceImp : MarkerLocalDataSource {

    override fun saveMarker(placeId : String, marker : Marker) {
        val config = RealmConfiguration.create(schema = setOf(MarkerEntity::class))
        val realm: Realm = Realm.open(config)

        realm.writeBlocking {
            copyToRealm(MarkerEntity().apply {
                this.placeId = placeId
                this.markerObject = marker
            })
        }
        realm.close()
    }
    override fun getMarker() {

    }
    override fun updateMarker() {

    }
}