package com.android.aviro.presentation.Home.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.aviro.BuildConfig.NAVER_CLIENT_ID
import com.android.aviro.databinding.FragmentMapBinding
import com.naver.maps.map.NaverMapSdk

class Map : Fragment() {

    private var _binding: FragmentMapBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val mapViewModel =
                ViewModelProvider(this).get(MapViewModel::class.java)

            _binding = FragmentMapBinding.inflate(inflater, container, false)
            val root: View = binding.root

            NaverMapSdk.getInstance(requireContext()).client =
                NaverMapSdk.NaverCloudPlatformClient("${NAVER_CLIENT_ID}")

            return root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}