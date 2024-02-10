package com.android.aviro.presentation.home.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.aviro.R
import com.android.aviro.databinding.FragmentChallengeBinding
import com.android.aviro.presentation.home.ui.map.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    //private val viewmodel: MypageViewModel by hiltNavGraphViewModels(R.id.navigation_mypage)
    private val viewmodel: MypageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        val bottomSheetView = layoutInflater.inflate(R.layout.challenge_guide_bottomsheet_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.setCanceledOnTouchOutside(false) // 외부 화면 클릭시 취소 방지
        bottomSheetDialog.setCancelable(false) // 하드웨어 백버튼 클릭시 취소 방지

        binding.infoBtn.setOnClickListener {
            bottomSheetDialog.show()
            val cancleBtn = bottomSheetView.findViewById<Button>(R.id.gotItBtn)
            cancleBtn.setOnClickListener {
                bottomSheetDialog.cancel()
            }
        }

        // 새로고침

        binding.challengScroll.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            if(!view.canScrollVertically(-1)) {
                viewmodel.getMyInfo()
                viewmodel.getChallengeInfo()
            }
        }


        binding.mypageBtn.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter, // 오른쪽에서 들어올 때의 애니메이션
                R.anim.slide_left_exit,      // 왼쪽으로 나갈 때의 애니메이션
                R.anim.slide_left_enter,   // 왼쪽에서 들어올 때의 애니메이션
                R.anim.slide_right_exit      // 오른쪽으로 나갈 때의 애니메이션
            )
            fragmentManager.replace(R.id.fragment_challenge_main, MypageFragment())
                .addToBackStack("mypage_fragment")
                .commit()

        }

        viewmodel.errorLiveData.observe(this, androidx.lifecycle.Observer {
            if(it != null) {
                Toast.makeText(requireContext(), it, LENGTH_SHORT).show()
            }
        })


        return root
    }

    override fun onResume() {
        super.onResume()
        Log.d("ChallengeFragment","onResume")
        viewmodel.getMyInfo()
        viewmodel.getChallengeInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}