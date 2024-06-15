package com.aviro.android.presentation.mapper

import com.aviro.android.domain.entity.restaurant.TimetableUpdating
import com.aviro.android.presentation.entity.UpdatingTimetableEntity


fun UpdatingTimetableEntity.toTimetableUpdating() : TimetableUpdating {
    return TimetableUpdating(
        mon = this.mon,
        monBreak = this.monBreak,
        tue = this.tue,
        tueBreak = this.tueBreak,
        wed = this.wed,
        wedBreak = this.wedBreak,
        thu = this.thu,
        thuBreak = this.thuBreak,
        fri = this.fri,
        friBreak = this.friBreak,
        sat = this.sat,
        satBreak = this.satBreak,
        sun = this.sun,
        sunBreak = this.sunBreak
    )
}

