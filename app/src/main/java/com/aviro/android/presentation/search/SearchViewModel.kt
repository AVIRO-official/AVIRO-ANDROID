package com.aviro.android.presentation.search

import android.Manifest
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.R
import com.aviro.android.domain.entity.search.SearchedRestaurantItem
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.search.SearchedRestaurantList
import com.aviro.android.domain.usecase.retaurant.SearchRestaurantUseCase
import com.aviro.android.presentation.entity.ItemAdapter
import com.aviro.android.presentation.entity.SortingLocEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRestaurantUseCase: SearchRestaurantUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // 내부저장소에서 이전에 검색했던 검색어 추출
    val prefs = context.getSharedPreferences("pre_searched_data", Context.MODE_PRIVATE)
    //private var preSearchedList = prefs.getString("pre_searched_data", "")
    private lateinit var preSearchedList : MutableMap<String, Int>
    //private var preSearchedSet = prefs.getStringSet("pre_searched_data", mutableSetOf()) ?: mutableSetOf()

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    private val _isPreSearched = MutableLiveData<Boolean>()
    val isPreSearched: LiveData<Boolean> = _isPreSearched

    val _isProgress = MutableLiveData<Boolean>()
    var isProgress : LiveData<Boolean> = _isProgress

    val _SrotingLocation = MutableLiveData<SortingLocEntity>() // 현재 정령할 기준을 담음
    var SrotingLocation : LiveData<SortingLocEntity> = _SrotingLocation

    val _SortLocText = MutableLiveData<String>()
    var SortLocText : LiveData<String> = _SortLocText

    val _SortAccText = MutableLiveData<String>()
    var SortAccText : LiveData<String> = _SortAccText

    private val _isSearchedList = MutableLiveData<Boolean>() // 가게 리스트 있는지 여부
    var isSearchedList : LiveData<Boolean> = _isSearchedList

    private val _adapterSearchedList = MutableLiveData<ItemAdapter>() // 가게 리스트 어댑터 형식으로
    var adapterSearchedList : LiveData<ItemAdapter> = _adapterSearchedList

    private val _searchList = MutableLiveData<List<SearchedRestaurantItem>>() // 가게 리스트
    var searchList : LiveData<List<SearchedRestaurantItem>> = _searchList


    private val _selectedSearchedItem = MutableLiveData<SearchedRestaurantItem>() // 가게 리스트
    var selectedSearchedItem : LiveData<SearchedRestaurantItem> = _selectedSearchedItem

    private val _preSearchedItems = MutableLiveData<List<String>?>() // 가게 리스트
    var preSearchedItems : LiveData<List<String>?> = _preSearchedItems

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> get() = _isRegistered

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    var centerOfMapX : Double? = null
    var centerOfMapY : Double? = null
    private var Keyword  = ""
    var currentPage  = 1
    var isEnd  = true
    var searchListSize = 0
    var isNewKeyword = false


    init {
        _isSearching.value = false //검색중 아님
        /*
        if(prefs.getStringSet("pre_searched_data", null) != null) {
            preSearchedSet = prefs.getStringSet("pre_searched_data", null)!!
        } else {
            preSearchedSet = LinkedHashSet()
        }
         */

        setPreSearchedWord()
        _isProgress.value = false
        _isSearchedList.value = false

        _SortLocText.value = "내위치중심"
        _SortAccText.value = "정확도순"

    }

    fun getCurrentGPSLoc(): Location? {
        val manager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 위치 기준
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) //manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else {
                // 위치 권한이 없는 경우 요청하도록 구현
                // 이 부분에서 위치 권한을 요청하는 코드를 추가해야 합니다.
                // 예를 들어, requestLocationPermission() 함수를 호출하는 등의 방식으로 위치 권한을 요청할 수 있습니다.
            }
        }
        // 맵 기준
        return null
    }

    /* 수정사항 : 가게 검색과 비건 유형 매칭 로직을 UseCase에서 한 번에 처리 (뷰모델에서는 완성된 결과 데이터만 반환 해주세요) */
    fun initList() {
        _isProgress.value = true
        viewModelScope.launch {
            searchRestaurantUseCase.getSearchedRestaurantList(
                Keyword,
                _SrotingLocation.value!!.x,
                _SrotingLocation.value!!.y,
                1,
                15,
                _SrotingLocation.value!!.sort
            ).let {

                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as SearchedRestaurantList

                        if(data.searchedList.size == 0) {
                            _isSearchedList.value = false
                        } else {
                            _isSearchedList.value = true

                            isNewKeyword = true
                            isEnd = data.is_end

                            _searchList.value = data.searchedList
                            searchListSize = data.searchedList.size

                        }

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
                Keyword,
                _SrotingLocation.value!!.x,
                _SrotingLocation.value!!.y,
                currentPage,
                15,
                _SrotingLocation.value!!.sort
            ).let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as SearchedRestaurantList

                        isNewKeyword = false
                        isEnd = data.is_end

                        //val preList = _searchList.value!!
                        val preSize = searchListSize

                        //_searchList.value = preList + data.searchedList
                        _searchList.value = _searchList.value?.plus(data.searchedList)
                        searchListSize = preSize + data.searchedList.size
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

    fun checkIsRegister(title : String, address : String, x : Double, y :Double)  {
        viewModelScope.launch {
            searchRestaurantUseCase.getIsRegistered(title, address, x, y).let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as Boolean
                        _isRegistered.value = data
                    }

                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message

                    }
                }
            }
        }
    }

    // editText에 값이 입력될때
    fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        Keyword = s.toString()
        currentPage = 1
        initList()
    }

    // 포커스 여부에 따라 키보드
    fun onEditTextFocusChanged(editTextView : View, hasFocus : Boolean) {
        _isSearching.value = hasFocus
        val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if(hasFocus) inputMethodManager.showSoftInput(editTextView, InputMethodManager.SHOW_IMPLICIT) else inputMethodManager.hideSoftInputFromWindow(editTextView.windowToken, 0)
        if(hasFocus) editTextView.requestFocus()  else editTextView.clearFocus()
    }

    // 검색어가 editText 에 들어욤
    fun searchRestaurant(view: View, keyCode: Int, event: KeyEvent) : Boolean {
        // 엔터 누르면 검색
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            // Enter 키를 눌렀을 때 수행할 작업
            // 예: 키보드 숨김
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    fun sortCenterOfMyLoc() {
        _SortLocText.value = "내위치중심"
        getCurrentGPSLoc()?.let {
            _SrotingLocation.value!!.x = getCurrentGPSLoc()!!.longitude.toString()
            _SrotingLocation.value!!.y = getCurrentGPSLoc()!!.latitude.toString()

        }?:run {
            _SrotingLocation.value!!.x = centerOfMapX.toString()
            _SrotingLocation.value!!.y = centerOfMapY.toString()

        }
        initList()
    }

    fun sortCenterOfMapLoc() {
        _SortLocText.value = "지도중심"
        _SrotingLocation.value!!.x = centerOfMapX.toString()
        _SrotingLocation.value!!.y = centerOfMapY.toString()
        initList()
    }

    fun sortAccuracy() {
        _SortAccText.value = "정확도순"
        _SrotingLocation.value!!.sort = "accuracy"
        initList()
    }

    fun sortDistance() {
        _SortAccText.value = "거리순"
        _SrotingLocation.value!!.sort = "distance"

        initList()
    }



    // 실제 remote 통해 검색한 경우에만 저장
    fun storeSearchedWord(search_word : String) {
        preSearchedList!!.put(search_word, 1)

        val json = Gson().toJson(preSearchedList)
        prefs.edit().putString("pre_searched_data", json).apply()

        setPreSearchedWord()
    }



    // 이전 검색어 지우기 (단일, 모두)
    fun setPreSearchedWord() {

        val json = prefs.getString("pre_searched_data", null)
        val type = object : TypeToken<Map<String, Any>>() {}.type

        preSearchedList = Gson().fromJson(json, type) ?: mutableMapOf()

        var newPreSearchedList = mutableListOf<String>()
        for (key in preSearchedList.keys) {
            newPreSearchedList.add(key)
        }
        _preSearchedItems.value = newPreSearchedList


        if(_preSearchedItems.value!!.size == 0){
            _isPreSearched.value = false
        }else {
            if(_preSearchedItems.value!!.size == 0) {
                _isPreSearched.value = false
            } else {
                _isPreSearched.value = true
            }
        }

    }

    fun removeAllPreSearchedWords() {
        prefs.edit().clear().apply()
        setPreSearchedWord()
    }
    fun removePreSearchedWord(item : String) {
        preSearchedList!!.remove(item)

        val json = Gson().toJson(preSearchedList)
        prefs.edit().putString("pre_searched_data", json).apply()

        setPreSearchedWord()

    }


    fun onClickItem(selected_search_item : SearchedRestaurantItem) {
        _selectedSearchedItem.value = selected_search_item
    }

    // 검색바 뒤로가기
    fun onClickBack(view: View) {
        //_isSearching.value = false
        val editText = view.rootView.findViewById<EditText>(R.id.EditTextSearchBar)
        onEditTextFocusChanged(editText,false)

    }


}