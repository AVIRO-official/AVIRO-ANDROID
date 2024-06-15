package com.aviro.android.presentation.bottomsheet

import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.challenge.ChallengeComment
import com.aviro.android.domain.entity.member.MemberLevelUp
import com.aviro.android.domain.usecase.challenge.GetChallengeInfo
import com.aviro.android.domain.usecase.member.UpdateMyReviewUseCase
import com.aviro.android.domain.usecase.retaurant.CreateRestaurantReviewUseCase
import com.aviro.android.presentation.entity.RestaurantInfoForReviewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor (
    private val getChallengeInfo : GetChallengeInfo,
    private val createRestaurantReviewUseCase : CreateRestaurantReviewUseCase,
    private val updateMyReviewUseCase : UpdateMyReviewUseCase
) : ViewModel() {

    var _restaurantInfo = MutableLiveData<RestaurantInfoForReviewEntity>()
    val restaurantInfo : LiveData<RestaurantInfoForReviewEntity>
        get() = _restaurantInfo

    var _reviewText = MutableLiveData<String>().apply {
        value = ""
    }
    val reviewText : LiveData<String>
        get() = _reviewText

    var _challengeComment = MutableLiveData<ChallengeComment>()
    val challengeComment : LiveData<ChallengeComment>
        get() = _challengeComment

    private val _levelUp = MutableLiveData<MemberLevelUp>()
    val levelUp: LiveData<MemberLevelUp> get() = _levelUp

    var _isWriting = MutableLiveData<Boolean>()
    val isWriting : LiveData<Boolean>
        get() = _isWriting

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData


    private val _toastLiveData = MutableLiveData<String?>()
    val toastLiveData: LiveData<String?> get() = _toastLiveData

    init {
        //_isWriting.value = false
        getComment()
    }

    fun afterTextChanged(s : Editable) {
        _reviewText.value = s.toString()
    }

    fun getComment() {
        viewModelScope.launch {
            getChallengeInfo.getChallengeComment().let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        _challengeComment.value = it.data as ChallengeComment
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }
            }
        }
    }

    // 후기 작성 버튼 클릭
    fun saveReview() {
        //_levelUp.value = MemberLevelUp(true, 1)
        viewModelScope.launch {
            val reviewId = UUID.randomUUID().toString()
            createRestaurantReviewUseCase.invoke(reviewId, _restaurantInfo.value!!.placeId, reviewText.value!!).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        // 레벨업 여부 확인
                        if(it.data != null) {
                            _levelUp.value = it.data as MemberLevelUp
                            Log.d("레벨업여부", "${_levelUp.value}")

                            //_toastLiveData.value = null

                        }
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }

            }
        }
    }

    fun updateReview(commentId : String) {
        viewModelScope.launch {
            updateMyReviewUseCase(commentId, reviewText.value!!).let {
                Log.d("updateReview", "${it}")
                when(it) {
                    is MappingResult.Success<*> -> {
                        _toastLiveData.value = it.message ?: "후기를 수정했습니다."
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message ?: "후기에 수정에 실패했습니다.\n다시 시도해주세요."
                    }
                }

            }
        }
    }

    fun onEditTextFocusChanged(view : View, keyCode: Int, event: KeyEvent) : Boolean { //hasFocus : Boolean
        val keyboardVisible = ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime())
        //_isWriting.value = (keyboardVisible ==  true)

        return true
    }

}