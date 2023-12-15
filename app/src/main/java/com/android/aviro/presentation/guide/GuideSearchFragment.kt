package com.android.aviro.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.aviro.BuildConfig
import com.android.aviro.databinding.FragmentGuideSearchBinding
import com.android.aviro.presentation.Home.ui.map.MapViewModel
import com.naver.maps.map.NaverMapSdk

class GuideSearchFragment : Fragment() {

    private var _binding: FragmentGuideSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGuideSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}