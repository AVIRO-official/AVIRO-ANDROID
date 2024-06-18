package com.aviro.android.presentation.home.ui.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.aviro.android.R
import com.aviro.android.databinding.FragmentMypageBinding
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.sign.Sign
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    // 이전 프래그먼트의 뷰모델을 가져와야 함 -> 아니면 그냥 새로은거 써도 될듯,,,
    val viewmodel: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        binding.versionCode.text = com.aviro.android.BuildConfig.VERSION_NAME // 현재 버전 정보
        initListener()

        return root


    }

    fun initListener() {
    binding.backBtn.setOnClickListener {
        val fragmentManager = parentFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
        setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
        fragmentManager.remove(this@MypageFragment).commit()

    }


    binding.manubarNickname.setOnClickListener {
        // parentFragmentManager, childFragmentManager 차이점은?
        val fragmentManager = parentFragmentManager.beginTransaction() //childFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(
            R.anim.slide_right_enter,
            R.anim.slide_left_exit,
            R.anim.slide_right_enter,
            R.anim.slide_left_exit
        )

        val nickname_frag = NicknameChangeFragment()
        fragmentManager.replace(R.id.fragment_mypage_main, nickname_frag)
            .addToBackStack(null)
            .commit()
    }

    binding.menubarInsta.setOnClickListener {
        val instagram_intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/aviro.kr.official"))
        startActivity(instagram_intent)
    }

        // 튜토리얼 처음부터 다시 보기
        binding.menubarTutorial.setOnClickListener {
            // 첫방문자인 것처럼 구현
            val prefs = requireContext().getSharedPreferences("aviro", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("showMapTutorial", true).apply()
            prefs.edit().putBoolean("showChallengeTutorial", true).apply()

            // 맵화면 이동
           // homeViewmodel._isNavigation.value = true
            val fragmentManager = parentFragmentManager.beginTransaction()
            setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToMap"))
            fragmentManager.remove(this@MypageFragment).commit()

        }

    binding.menubarQuestion.setOnClickListener {
        val email_intent = Intent(Intent.ACTION_SEND)
        //email_intent.setPackage("com.google.android.gm")
        email_intent.type = "*/*"
        val emailArray = arrayOf("aviro.kr.official@gmail.com")
        email_intent.putExtra(Intent.EXTRA_EMAIL, emailArray) // 받는 사람 이메일
        email_intent.putExtra(Intent.EXTRA_SUBJECT, "[AVIRO] ${viewmodel.nickname.value}님의 문의 및 의견") // 메일 제목
        startActivity(email_intent)
    }

    binding.menubarOpensource.setOnClickListener {
        startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
        OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
    }

    binding.logoutBtn.setOnClickListener {
        AviroDialogUtils.createTwoDialog(
            requireContext(),
            "로그아웃 하시겠어요?",
            null,
            "취소",
            "로그아웃",
            { viewmodel.logout() }
        ).show()

    }

    binding.withdrawBtn.setOnClickListener {
        AviroDialogUtils.createTwoDialog(
            requireContext(),
            "정말로 어비로를 떠나시는 건가요?",
            "회원탈퇴 이후, 내가 등록한 가게와 댓글은 사라지지 않지만 다시 볼 수 없어요.\n정말로 탈퇴하시겠어요?",
            "취소",
            "탈퇴하기",
            { viewmodel.withdraw() }
        ).show()
    }

    viewmodel.movingScreen.observe(viewLifecycleOwner) {
        when (it) {
            R.string.LOGOUT.toString() -> {
                val intent = Intent(requireContext(), Sign::class.java)
                intent.putExtra("Action", R.string.LOGOUT.toString())
                startActivity(intent)
            }
            R.string.WITHDRAW.toString() -> {
                val intent = Intent(requireContext(), Sign::class.java)
                intent.putExtra("Action", R.string.WITHDRAW.toString())
                startActivity(intent)
            }
        }

    }

    binding.MainLinearLayout.setOnTouchListener(object: OnSwipeTouchListener(context = requireContext()) {
        override fun onSwipeLeft() {
            Log.d("Swipe","onLeft")
        }
        override fun onSwipeRight() {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            fragmentManager.remove(this@MypageFragment).commit()
        }
    })
}


}


abstract class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    companion object {
        // 반응성 조절
        private const val SWIPE_DISTANCE_THRESHOLD = 200
        private const val SWIPE_VELOCITY_THRESHOLD = 200
    }
    private val gestureDetector: GestureDetector
    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val distanceX = e2.x - e1!!.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) > Math.abs(distanceY)
                && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) {
                    onSwipeRight()
                } else onSwipeLeft()
                return true
            }
            return false
        }
    }
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}


