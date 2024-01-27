package com.android.aviro.presentation.search

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.aviro.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // 내부저장소에서 이전에 검색했던 검색어 추출
    val prefs = context.getSharedPreferences("pre_searched_data", Context.MODE_PRIVATE)
    val searchedSet = prefs.getStringSet("searchedSet", setOf()) // 처음엔 default 값 출력

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    private val _isPreSearched = MutableLiveData<Boolean>()
    val isPreSearched: LiveData<Boolean> = _isPreSearched

    init {
        _isSearching.value = false //검색중 아님
        _isPreSearched.value = searchedSet!!.size != 0
    }

    // 실제 remote 통해 검색한 경우에만 저장
    fun storeSearchedWord(search_word : String) {
        searchedSet!!.add(search_word)
        prefs.edit().putStringSet("searchedSet", searchedSet).apply()
        _isPreSearched.value = true
    }


    // 검색창 클릭하면 바로 활성화
    fun onEditTextFocusChanged(editText : View, hasFocus : Boolean) {
        //Log.d("hasFocus","${hasFocus}")
        _isSearching.value = hasFocus
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if(hasFocus) inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT) else inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)

    }


    // 검색어가 editText 에 들어욤
    fun searchRestaurant(editText : EditText) {
        val keyword = editText.text
        // remote 검색 요청


    }

    // 검색바 뒤로가기
    fun onClickBack() {
        _isSearching.value = false

    }

    // 검색중인 단어 제거
    fun onClickCancleBtn() {

    }

    // 이전 검색어 지우기 (단일, 모두)


    fun setIsSearching() {
        _isSearching.value = !(isSearching.value)!!
    }


}