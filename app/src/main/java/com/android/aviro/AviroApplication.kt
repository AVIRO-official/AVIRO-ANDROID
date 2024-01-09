package com.android.aviro

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AviroApplication : Application() { // @Inject constructor (private val createSocialAccountUseCase: CreateSocialAccountUseCase)

    override fun onCreate() {
        super.onCreate()

        // Realem 초기화
        // Realm 초기화
        /*Realm.init(this)
        val config : RealmConfiguration = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .name("restaurant.realm") // 생성할 realm db 이름
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(config)*/


    }

}