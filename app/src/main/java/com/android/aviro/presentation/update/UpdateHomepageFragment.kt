package com.android.aviro.presentation.update

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentUpdateHomepageBinding

class UpdateHomepageFragment : Fragment() {

    //private val sharedViewModel: SignViewModel by activityViewModels()

    private var _binding: FragmentUpdateHomepageBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        Log.d("프래그먼트 생명주기","onAttach")
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("프래그먼트 생명주기", "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateHomepageBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewStateRestored(viewStateRestored: Bundle?) {
        Log.d("프래그먼트 생명주기","onViewStateRestored")
        super.onViewStateRestored(viewStateRestored)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("프래그먼트 생명주기","onViewCreated")
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onStart() {
        Log.d("프래그먼트 생명주기","onStart")
        super.onStart()

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

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("프래그먼트 생명주기","onSaveInstanceState")
        super.onSaveInstanceState(outState)

    }


    override fun onStop() {
        Log.d("프래그먼트 생명주기","onStop")
        super.onStop()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("프래그먼트 생명주기", "onDestroyView")
        _binding = null
    }

    override fun onLowMemory() {
        Log.d("프래그먼트 생명주기", "onLowMemory")
        super.onLowMemory()

    }

    override fun onDestroy() {
        Log.d("프래그먼트 생명주기", "onDestroy")
        super.onDestroy()


    }



}