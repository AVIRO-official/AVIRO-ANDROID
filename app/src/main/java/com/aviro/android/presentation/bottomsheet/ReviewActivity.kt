package com.aviro.android.presentation.bottomsheet

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.aviro.android.R
import com.aviro.android.common.OnKeyboardVisibilityListener
import com.aviro.android.databinding.FragmentReviewScriptBinding
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.entity.RestaurantInfoForReviewEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewActivity : FragmentActivity() {

    private lateinit var binding : FragmentReviewScriptBinding
    private val viewmodel : ReviewViewModel by viewModels()

    var reviewcontent : Review? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        binding = FragmentReviewScriptBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        initUI()
        initListener()
        initObserver()
        setAnimation()

    }

    fun initUI() {
        // 가게정보 표시
        viewmodel._restaurantInfo.value = intent.getParcelableExtra<RestaurantInfoForReviewEntity>("restaurant")!!

        // 후기 수정하기인 경우
        reviewcontent = intent.getParcelableExtra<Review>("review")
        reviewcontent?.let {
            viewmodel._reviewText.value = it.content
        }

    }


    private fun initListener() {
        binding.backBtn.setOnClickListener {
            // 정말로 삭제할건지 물어보기
            AviroDialogUtils.createTwoDialog(this,
                "정말로 후기 작성을 취소하나요?",
                "작성하던 후기가 모두 삭제됩니다.",
                "아니요",
                "예",
                {
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }).show()

        }

        /*
        binding.reviewScriptEditTextView.setOnFocusChangeListener { view, b ->
            Log.d("setOnFocusChangeListener", "${b}")
             binding.infoContainer.visibility = if(b) View.GONE else View.VISIBLE

        }*/

        binding.completeBtn.setOnClickListener {
            Log.d("ReviewActivity", "${reviewcontent}")
            reviewcontent?.let {
                // 수정
                viewmodel.updateReview(it.commentId)

                // 추천 권유 없음
                intent.putExtra("recommend", false)
                setResult(Activity.RESULT_OK, intent)

                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

            } ?: run {
                // 신규
                viewmodel.saveReview()

            }


        }

        setKeyboardVisibilityListener(object : OnKeyboardVisibilityListener {
            override fun onVisibilityChanged(visible: Boolean) {
                if (visible) {
                    binding.infoContainer.startAnimation(AnimationUtils.loadAnimation(this@ReviewActivity, R.anim.slide_up_hide_bottom))
                    binding.infoContainer.visibility = View.GONE
                    //binding.infoContainer.animation = AnimationUtils.loadAnimation(this@ReviewActivity, R.anim.slide_up_hide_bottom)
                    //binding.infoContainer.visibility = View.GONE
                } else {
                    binding.infoContainer.visibility = View.VISIBLE
                    binding.infoContainer.animation = AnimationUtils.loadAnimation(this@ReviewActivity, R.anim.slide_down_show)
                }
            }
        })



    }

    private fun initObserver() {
        viewmodel.reviewText.observe(this) {
            if(it.length >= 10) {
                binding.completeBtn.background = ContextCompat.getDrawable(this, R.color.Cobalt)
                binding.completeTextView.setTextColor(ContextCompat.getColor(this,R.color.Gray7))
                binding.reviwTextAmount.setText(it.length.toString())
                binding.reviwTextAmount.setTextColor(ContextCompat.getColor(this,R.color.Cobalt))
                binding.completeBtn.isEnabled = true

                if(it.length > 200) {
                    val shakeAnimation: Animation =
                        AnimationUtils.loadAnimation(this, R.anim.wrong_shake)
                    binding.reviewContainer.startAnimation(shakeAnimation)
                }
            } else {
                binding.completeBtn.background = ContextCompat.getDrawable(this, R.color.Gray5)
                binding.completeTextView.setTextColor(ContextCompat.getColor(this,R.color.Gray2))
                binding.reviwTextAmount.setText(it.length.toString())
                binding.reviwTextAmount.setTextColor(ContextCompat.getColor(this,R.color.Warn_Red))
                binding.completeBtn.isEnabled = false
            }
        }


        // 무조건 받음
        viewmodel.levelUp.observe(this) {
            // 레벨업 여부 확인
            Log.d("레벨업여부", "${viewmodel.levelUp.value}")
            if(it.levelUp) {
                intent.putExtra("level_up", it)
                setResult(Activity.RESULT_OK, intent)

            }
            // 추천 권유
            intent.putExtra("recommend", true)
            setResult(Activity.RESULT_OK, intent)

            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        viewmodel.toastLiveData.observe(this) {

            it?.let {
                val toast = Toast.makeText(this, it, Toast.LENGTH_SHORT)
                toast.show()
            }


        }

        viewmodel.errorLiveData.observe(this) {
            it?.let { errorMsg ->
                AviroDialogUtils.createOneDialog(
                    this,
                    "Error",
                    "${it}",
                    "확인"
                ).show()
            }
        }


    }

    private fun setAnimation() {
        val animator =  binding.commentBlow.animate().apply {
            translationYBy(20f)
            duration = 400L
            // 애니메이션이 끝난 후에 위로 20픽셀 이동하는 애니메이션을 추가합니다.
            withEndAction {
                binding.commentBlow.animate().apply {
                    translationYBy(-20f)
                    duration = 200L
                    // 애니메이션이 끝나면 다시 아래로 20픽셀 이동하는 애니메이션을 시작합니다.
                    withEndAction { setAnimation() } // 재귀로 무한반복
                }.start()
            }
        }
        animator.start()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // 키가 눌렸을 때 호출되는 메서드
        Log.d("onKeyDown", "${keyCode}, ${event}")
        //if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.ACTION_DOWN) {
            binding.reviewScriptEditTextView.focusable = View.NOT_FOCUSABLE
            return true // 이벤트가 소비되었음을 반환
        //}
        //return super.onKeyDown(keyCode, event)
    }


    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView = (binding.root as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + 48
            private val rect = Rect()

            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.resources.displayMetrics
                ).toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen) {

                    return
                }
                alreadyOpen = isShown
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }


}