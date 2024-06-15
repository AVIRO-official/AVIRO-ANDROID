package com.aviro.android.presentation.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.databinding.FragmentUpdateHomepageBinding

class UpdateHomepageFragment : Fragment() {

    private var _binding: FragmentUpdateHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewmodel : UpdateViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateHomepageBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        val root: View = binding.root

        initUI()


        return root
    }

    fun initUI() {

    }



    override fun onResume() {
        Log.d("프래그먼트 생명주기","onResume")
        super.onResume()
        //naver_map = viewmodel.getNaverMap()

    }

    override fun onPause() {
        Log.d("프래그먼트 생명주기","onPause")
        super.onPause()


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}