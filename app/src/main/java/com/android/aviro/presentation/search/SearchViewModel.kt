package com.android.aviro.presentation.search

import android.Manifest
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.search.SearchedRestaurantList
import com.android.aviro.domain.usecase.retaurant.SearchRestaurantUseCase
import com.android.aviro.presentation.entity.ItemAdapter
import com.android.aviro.presentation.entity.SortingLocEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Collections.addAll
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRestaurantUseCase: SearchRestaurantUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // 내부저장소에서 이전에 검색했던 검색어 추출
    val prefs = context.getSharedPreferences("pre_searched_data", Context.MODE_PRIVATE)
    val searchedSet = mutableSetOf<String>() // 처음엔 default 값 출력

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    private val _isPreSearched = MutableLiveData<Boolean>()
    val isPreSearched: LiveData<Boolean> = _isPreSearched

    val _isProgress = MutableLiveData<Boolean>()
    var isProgress : LiveData<Boolean> = _isProgress

    val _SrotingLocation = MutableLiveData<SortingLocEntity>() // 현재 정령할 기준을 담음
    var SrotingLocation : LiveData<SortingLocEntity> = _SrotingLocation

    private val _SortLocText = MutableLiveData<String>()
    var SortLocText : LiveData<String> = _SortLocText

    private val _SortAccText = MutableLiveData<String>()
    var SortAccText : LiveData<String> = _SortAccText

    //private val _SearchedList = MutableLiveData<List<SearchedRestaurantItem>>() // 가게 리스트
    //var SearchedList : LiveData<List<SearchedRestaurantItem>> = _SearchedList

    private val _adapterSearchedList = MutableLiveData<ItemAdapter>() // 가게 리스트
    var adapterSearchedList : LiveData<ItemAdapter> = _adapterSearchedList

    private val _selectedSearchedItem = MutableLiveData<SearchedRestaurantItem>() // 가게 리스트
    var selectedSearchedItem : LiveData<SearchedRestaurantItem> = _selectedSearchedItem

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    var centerOfMapX : String? = null
    var centerOfMapY : String? = null
    private var searchedKeyword  = ""
    var currentPage  = 1
    var isEnd  = true


    init {
        _isSearching.value = false //검색중 아님
        _isPreSearched.value = !prefs.all.isEmpty()
        _isProgress.value = false

        _SortLocText.value = "내위치중심"
        _SortAccText.value = "정확도순"

    }

    fun getCurrentGPSLoc(): Location? {
        val manager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           // 위치 기준
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

                return manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

        }
        // 맵 기준
        return null
    }

    /* 수정사항 : 가게 검색과 비건 유형 매칭 로직을 UseCase에서 한 번에 처리 (뷰모델에서는 완성된 결과 데이터만 반환 해주세요) */
    fun initList() {
        _isProgress.value = true
        viewModelScope.launch {
            Log.d("keyword", "${searchedKeyword}")
            searchRestaurantUseCase.getSearchedRestaurantList(
                searchedKeyword,
                _SrotingLocation.value!!.x,
                _SrotingLocation.value!!.y,
                1,
                15,
                _SrotingLocation.value!!.sort
            ).let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        Log.d("initList","${it.data}")
                        val data = it.data as SearchedRestaurantList
                        isEnd = data.is_end
                        //_SearchedPlaceList.value = data.searchedList
                        _adapterSearchedList.value =
                            ItemAdapter(true, data.searchedList, data.searchedList.size)
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                    else -> {}
                }
                _isProgress.value = false
            }
        }
    }


    // usecase 응답 결과의 isEnd == false 일 경우, 다음 리스트 불러와 합침
    fun nextList() {
        _isProgress.value = true
        viewModelScope.launch {
            searchRestaurantUseCase.getSearchedRestaurantList(
                searchedKeyword,
                _SrotingLocation.value!!.x,
                _SrotingLocation.value!!.y,
                currentPage,
                15,
                _SrotingLocation.value!!.sort
            ).let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as SearchedRestaurantList
                        isEnd = data.is_end

                        _adapterSearchedList.value!!.isNewKeyword = false
                        _adapterSearchedList.value!!.itemList.apply { addAll(data.searchedList.toMutableList()) }
                        _adapterSearchedList.value!!.addingCount = data.searchedList.size
                    }

                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                    else -> {}
                }
                _isProgress.value = false
            }

        }
    }

    // 실제 remote 통해 검색한 경우에만 저장
    fun storeSearchedWord(search_word : String) {
        //searchedSet!!.add(search_word)
        searchedSet.add(search_word)
        prefs.edit().putStringSet("searchedSet", searchedSet).apply()
        _isPreSearched.value = true
    }


    // 포커스 여부에 따라 키보드
    fun onEditTextFocusChanged(editTextView : View, hasFocus : Boolean) {
        Log.d("hasFocus","${hasFocus}")
        _isSearching.value = hasFocus
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if(hasFocus) inputMethodManager.showSoftInput(editTextView, InputMethodManager.SHOW_IMPLICIT) else inputMethodManager.hideSoftInputFromWindow(editTextView.windowToken, 0)
        if(hasFocus) editTextView.requestFocus()  else editTextView.clearFocus()

    }


    // 검색어가 editText 에 들어욤
    fun searchRestaurant(view: View, keyCode: Int, event: KeyEvent) : Boolean {
        val edit_text = view as EditText
        val keyword = edit_text.text.toString()
        // 엔터 누르면 검색
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            // Enter 키를 눌렀을 때 수행할 작업
            // 예: 키보드 숨김
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            // remote 검색 요청
            searchedKeyword = keyword
            currentPage = 1
            storeSearchedWord(keyword)
            initList()
            return true
        }
        return false
    }




    fun onClickLocSort(view: View) {
        // 내 위치 기준 // 맵 기준
        _SortLocText.value = if(_SortLocText.value == "내 위치 중심") "지도 중심" else "내위치중심"
        getCurrentGPSLoc()?.let {
            _SrotingLocation.value!!.x = getCurrentGPSLoc()!!.longitude.toString()
            _SrotingLocation.value!!.y = getCurrentGPSLoc()!!.latitude.toString()
        } ?: run {
            _SrotingLocation.value!!.x = centerOfMapX.toString()
            _SrotingLocation.value!!.y = centerOfMapY.toString()
        }

        // 리사이클러뷰 다 제거하고 다시 호출
        initList()
    }

    fun onClickAccSort(view: View) {
        // 정확도 기준  // 거리순 기준
        _SortAccText.value = if(_SrotingLocation.value!!.sort == "accuracy") "거리순" else "정확도순"
        _SrotingLocation.value!!.sort = if(_SrotingLocation.value!!.sort == "accuracy") "distance" else "accuracy"

        // 리사이클러뷰 다 제거하고 다시 호출
        initList()
    }

    // 검색바 뒤로가기
    fun onClickBack(view: View) {
        //_isSearching.value = false
        val editText = view.rootView.findViewById<EditText>(R.id.EditTextSearchBar)
        onEditTextFocusChanged(editText,false)

    }

    // 이전 검색어 지우기 (단일, 모두)


    fun setIsSearching() {
        _isSearching.value = !(isSearching.value)!!
    }

    fun onClickItem(selected_search_item : SearchedRestaurantItem) {
        Log.d("onClickItem", "${selected_search_item}")
        _selectedSearchedItem.value = selected_search_item

    }


}