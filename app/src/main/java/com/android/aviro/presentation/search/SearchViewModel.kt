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
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.data.entity.restaurant.SearchEntity
import com.android.aviro.domain.usecase.retaurant.SearchRestaurantUseCase
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
    val searchedSet = mutableSetOf<String>() // 처음엔 default 값 출력

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    private val _isPreSearched = MutableLiveData<Boolean>()
    val isPreSearched: LiveData<Boolean> = _isPreSearched

    private val _SearchedPlaceList = MutableLiveData<List<SearchEntity>>() // 가게 리스트
    var SearchedPlaceList : LiveData<List<SearchEntity>> = _SearchedPlaceList

    private var searchedKeyword  = ""

    init {
        _isSearching.value = false //검색중 아님
        _isPreSearched.value = !prefs.all.isEmpty()
        _SearchedPlaceList.value =  listOf(
            SearchEntity("","우리마트","3","121","123","","","",
                "이촌동","","url","010")
        )
    }

    fun initList() {
        viewModelScope.launch {
            Log.d("keyword","${searchedKeyword}")
            val response = searchRestaurantUseCase.getSearchedRestaurantList(
                searchedKeyword,
                "127.03596593951177",
                "37.5071726244927",
                1,
                15,
                "accuracy"
            )
            Log.d("response","${response}")
            response.onSuccess {
                Log.d("response","${it.documents}")
                _SearchedPlaceList.value = it.documents
            }
        }
        /*
        // 응답 결과는 메타 데이터까지 모두 갖고 있는 형태
        searchRestaurantUseCase.
        repository.list { plantList ->
            _plantList.postValue(plantList)
        }
         */
    }

    // usecase 응답 결과의 isEnd == false 일 경우, 다음 리스트 불러와 합침
    fun nextList() {
        /*
        val currentPlantList = plantList.value ?: return
        repository.next(currentPlantList) { palntList ->
            // 기존 list와 다음 list를 더해줍니다.
            val mergedPlants = currenPlantList.plants.toMutableList()
                .apply { addAll(plantList.plants) }
            plantList.plants = mergedPlants
            _plantList.postValue(plantList)
        }

         */
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
            storeSearchedWord(keyword)
            initList()
            return true
        }
        return false


    }

    // 검색바 뒤로가기
    fun onClickBack(view: View) {
        //_isSearching.value = false
        val editText = view.rootView.findViewById<EditText>(R.id.EditTextSearchBar)
        onEditTextFocusChanged(editText,false)

    }

    // 검색중인 단어 제거
    fun onClickCancleBtn() {

    }

    // 이전 검색어 지우기 (단일, 모두)


    fun setIsSearching() {
        _isSearching.value = !(isSearching.value)!!
    }


}