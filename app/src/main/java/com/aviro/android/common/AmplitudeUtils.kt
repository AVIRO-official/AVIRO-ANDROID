package com.aviro.android.common

import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.DefaultTrackingOptions
import com.amplitude.android.events.Identify
import com.aviro.android.BuildConfig
import com.aviro.android.data.datasource.datastore.DataStoreDataSource
import com.aviro.android.data.datasource.member.MemberDataSource
import com.aviro.android.data.model.menu.MenuDTO
import com.aviro.android.domain.entity.menu.Menu
import javax.inject.Inject


/*
interface AmplitudeUtils {
    fun init(amplitude : Amplitude)
}
*/

enum class AMType(
    val typeName : String
) {
    signUp("user_sign up"),
    withdrawal("user_withdrawal"),
    userLogin("user_login"),
    userLogout("user_logout"),

    placeUpload("place_upload"),
    reviewUpload("review_upload"),


    placeEdit("place_edit"),
    placeSearch("place_serach"),
    placePresent("place_present"),
    menuEdit("menu_edit"),

    challPresent("chall_present"),
    placeListPresent("placeList_present"),
    reviewListPresent("reviewList_present"),
    bookmarkListPresent("bookmarkList_present"),
    levelupDidMove("level_up_didMove"),
    levelupDidNotMove("level_up_didNotMove"),
    wellcomeClick("wellcome_click"),

    wellcomeNoShow("wellcome_noShow"),
    wellcomeClose("wellcome_close")

}


object AmplitudeUtils  {

    // Application 에서 한번만 하고, amplitude를 넘겨줌
    // 만약 amplitude 변수가 유효하지 않으면 다시 초기화
    var amplitude : Amplitude? = null
    fun init(amplitude : Amplitude) {
        this.amplitude = amplitude
    }

    fun login(userId : String , name : String?, email : String?, nickname : String, type: String) {
        val identify = Identify()

        amplitude?.setUserId(userId) // userID 정보 변경

        identify.set("name", name ?: "")
        identify.set("email",email ?: "")
        identify.set("nickname", nickname)
        identify.set("version", BuildConfig.VERSION_NAME)
        identify.set("type", type) // sns 값 추가

        amplitude?.identify(identify)
        amplitude?.track(AMType.userLogin.typeName)
    }

    fun signUp(userId: String) {
        amplitude?.setUserId(userId)
        amplitude?.track(AMType.signUp.typeName)
    }

    fun withdrawal() {
        amplitude?.track(AMType.withdrawal.typeName)
    }

    fun logout() {
        amplitude?.track(AMType.userLogout.typeName)
    }

    fun placeUpload(place: String) {
        amplitude?.track( AMType.placeUpload.typeName,
            mutableMapOf("Place" to place)
        )
    }
    fun reviewUpload(place: String, review: String) {
        amplitude?.track(AMType.reviewUpload.typeName,
            mutableMapOf("Place" to place, "Review" to review)
        )
    }

    fun placePresent(place: String) { // 가게명
        amplitude?.track(
             AMType.placePresent.typeName, mutableMapOf("Place" to place)
        )
    }

    fun placeSearch(query: String) {
        amplitude?.track( AMType.placeSearch.typeName,
            mutableMapOf("Query" to query)
        )
    }

    fun placeEdit(place: String) {
        amplitude?.track(
           AMType.placeEdit.typeName,
            mutableMapOf("Place" to place)
        )
    }
    fun menuEdit(place: String, beforeMenus: List<Menu>, afterMenus: List<Menu>) {

        var beforeMenusString = ""
        for((index, menu) in beforeMenus.withIndex()){
            val menuString = "${index + 1}: (${menu.menu} ${menu.price} ${menu.howToRequest}) "
            beforeMenusString += menuString
        }

        var afterMenusString = ""
        for((index, menu) in afterMenus.withIndex()){
            val menuString = "${index + 1}: (${menu.menu} ${menu.price} ${menu.howToRequest}) "
            afterMenusString += menuString
        }

        amplitude?.track(
          AMType.menuEdit.typeName,
            mutableMapOf("Place" to place,
        "BeforeChangedMenuArray" to beforeMenusString,
        "AfterChangedMenuArray" to afterMenusString
            )
        )

    }

    fun challengePresent() {
        amplitude?.track( AMType.challPresent.typeName)
    }

    fun placeListPresent() {
        amplitude?.track( AMType.placeListPresent.typeName)
    }

    fun reviewListPresent() {
        amplitude?.track( AMType.reviewListPresent.typeName)
    }

    fun bookmarkListPresent() {
        amplitude?.track(AMType.bookmarkListPresent.typeName)
    }

    fun levelupDidMove(level: Int) {
        amplitude?.track(
             AMType.levelupDidMove.typeName, mutableMapOf("level" to level)
        )
    }
    fun levelupDidNotMove(level: Int) {
        amplitude?.track(
             AMType.levelupDidNotMove.typeName, mutableMapOf("level" to level)
        )
    }

    fun didTappedCheckWelcome() {
        amplitude?.track(AMType.wellcomeClick.typeName)
    }

    fun didTappedNoMoreShowWelcome() {
        amplitude?.track( AMType.wellcomeNoShow.typeName)
    }

    fun didTappedCloseWelcome() {
        amplitude?.track( AMType.wellcomeClose.typeName)
    }

}



