package com.aviro.android.presentation.home.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.aviro.android.R
import com.aviro.android.databinding.FragmentChallengeBinding
import com.aviro.android.presentation.aviro_dialog.ChallengeGuideDialog
import com.aviro.android.presentation.home.Home
import com.aviro.android.presentation.home.HomeViewModel

//@AndroidEntryPoint
class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    //private val viewmodel: MypageViewModel by hiltNavGraphViewModels(R.id.navigation_mypage)
    private val viewmodel: MypageViewModel by activityViewModels()
    private val homeViewmodel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        initListener()
        initObserver()

        return root
    }


    @SuppressLint("ResourceType")
    fun initListener() {

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("resultKey")
            if(result == "MypageToChallenge") {
                viewmodel.getMyInfo()
                viewmodel.getChallengeInfo()

                viewmodel.getMyReviewList()
                viewmodel.getMyBookmarkList()
                viewmodel.getMyRestaurantList()
            } else if(result == "MypageToMap") {
                homeViewmodel._currentNavigation.value = HomeViewModel.WhereToGo.RESTAURANT
                homeViewmodel._isNavigation.value = true
            }

        }


        binding.infoBtn.setOnClickListener {
            val bottomSheetDialog = ChallengeGuideDialog(viewmodel)
            bottomSheetDialog.isCancelable = false
            bottomSheetDialog.show(parentFragmentManager, "challengeDialog")
        }

        binding.MyRestaurantList.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_right_enter,
                R.anim.slide_left_exit
            )
            fragmentManager.replace(R.id.fragment_challenge_main, MyRestaurantFrag())
                .addToBackStack(null)
                .commit()
        }

        binding.MyReviewList.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_right_enter,
                R.anim.slide_left_exit

            )
            fragmentManager.replace(R.id.fragment_challenge_main, MyReviewFrag())
                .addToBackStack(null)
                .commit()
        }

        binding.MyBookmarkList.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_right_enter,
                R.anim.slide_left_exit
            )
            fragmentManager.replace(R.id.fragment_challenge_main, MyBookmarkFrag())
                .addToBackStack(null)
                .commit()
        }


        binding.mypageBtn.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_right_enter,
                R.anim.slide_left_exit
            )
            fragmentManager.replace(R.id.fragment_challenge_main, MypageFragment())
                .addToBackStack(null)
                .commit()
        }

        // 새로고침
        binding.challengScroll.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            if(!view.canScrollVertically(-1)) {
                //viewmodel.getMyInfo()
                //viewmodel.getChallengeInfo()
            }
        }

    }

    fun initObserver() {
        viewmodel.errorLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null) {
                Toast.makeText(requireContext(), it, LENGTH_SHORT).show()
            }
        })
    }


    fun isFirstStartChallenge() : Boolean {
        val prefs = requireContext().getSharedPreferences("aviro", Context.MODE_PRIVATE)
        val firstChallengeRun = prefs.getBoolean("showChallengeTutorial", true) // 처음엔 default 값 출력

        if (firstChallengeRun) {
            prefs.edit().putBoolean("showChallengeTutorial", false).apply()
        }

        return firstChallengeRun
    }

    fun runTutorial() {
        // 튜토리얼3 시작
        val parentActivity = activity as Home
        val tutorial3 = parentActivity.findViewById<FrameLayout>(R.id.tutoral3)
        parentActivity.findViewById<ConstraintLayout>(R.id.home_container).isEnabled = false

        tutorial3.visibility = View.VISIBLE
        tutorial3.isEnabled = true

        parentActivity.findViewById<ImageView>(R.id.tutorialImg1).visibility = View.VISIBLE
        parentActivity.findViewById<ImageView>(R.id.tutorialImg2).visibility = View.VISIBLE

        tutorial3.setOnClickListener {
            parentActivity.findViewById<ImageView>(R.id.tutorialImg1).visibility = View.GONE
            parentActivity.findViewById<ImageView>(R.id.tutorialImg2).visibility = View.GONE
            tutorial3.visibility = View.GONE
            tutorial3.isEnabled = false
            parentActivity.findViewById<ConstraintLayout>(R.id.home_container).isEnabled = true
        }
    }



    override fun onResume() {
        super.onResume()
        homeViewmodel._isNavigation.value = false

        viewmodel.getMyInfo()  // 챌린지 정보 호출
        viewmodel.getChallengeInfo()

        viewmodel.getMyReviewList()
        viewmodel.getMyRestaurantList()
        viewmodel.getMyBookmarkList()

        if(isFirstStartChallenge()) {
            runTutorial()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}