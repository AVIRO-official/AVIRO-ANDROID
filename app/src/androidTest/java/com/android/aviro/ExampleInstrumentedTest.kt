package com.android.aviro

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    /* UI 테스트용으로 사용 */
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.android.aviro", appContext.packageName)
    }

    // 맵화면을 로드하면 가게 위치 데이터 마커가 표시되어야 한다
    // delete 버튼을 클릭하면 마커 데이터가 삭제되어야 한다
    // update 버튼을 클릭하면 마커 데이터가 업데이트 되어야 한다 (타입 수정, 위치 수정)

}