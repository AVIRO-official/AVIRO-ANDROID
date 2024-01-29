package com.android.aviro.presentation.home.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.aviro.R
import com.android.aviro.databinding.FragmentChallengeBinding
import com.android.aviro.presentation.home.ui.map.MapViewModel
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

        binding.mypageBtn.setOnClickListener {
            val fragmentManager = childFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter, // 오른쪽에서 들어올 때의 애니메이션
                R.anim.slide_left_exit,      // 왼쪽으로 나갈 때의 애니메이션
                R.anim.slide_left_enter,   // 왼쪽에서 들어올 때의 애니메이션
                R.anim.slide_right_exit      // 오른쪽으로 나갈 때의 애니메이션
            )
            fragmentManager.replace(R.id.fragment_mypage_main, MypageFragment())
                .addToBackStack("mypage_fragment")
                .commit()

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}