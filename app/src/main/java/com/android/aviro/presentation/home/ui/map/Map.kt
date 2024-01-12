package com.android.aviro.presentation.home.ui.map

import android.content.om.OverlayManagerTransaction.newInstance
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.aviro.BuildConfig.NAVER_CLIENT_ID
import com.android.aviro.R
import com.android.aviro.data.datasource.datastore.DataStoreDataSourceImp_Factory.newInstance
import com.android.aviro.databinding.FragmentMapBinding
import com.android.aviro.databinding.FragmentRegisterBinding
import com.android.aviro.presentation.sign.SignViewModel
import com.naver.maps.map.MapFragment.newInstance
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback



class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    //private val viewmodel: MapViewModel by viewModels()
    //private val viewmodel: MapViewModel by activityViewModels()
    private val viewmodel: MapViewModel by hiltNavGraphViewModels(R.id.navigation_map)

    //private lateinit var mapView: MapView

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            _binding = FragmentMapBinding.inflate(inflater, container, false)
            val root: View = binding.root

            //viewmodel = ViewModelProvider(this).get(MapViewModel::class.java)
            //viewmodel = MapViewModel()
            //binding.viewmodel = viewmodel
            //binding.lifecycleOwner = this

            NaverMapSdk.getInstance(requireContext()).client =
                NaverMapSdk.NaverCloudPlatformClient("${NAVER_CLIENT_ID}")



            var mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
            if (mapFragment == null) {
                mapFragment = MapFragment()
                childFragmentManager.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
            }
            mapFragment.


            viewmodel.isFirstStartMap()

            return root
        }


    override fun onStart() {
        super.onStart()
        binding.mapFragment.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapFragment.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapFragment.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapFragment.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapFragment.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapFragment.onLowMemory()
    }


    override fun onMapReady(p0: NaverMap) {
        TODO("Not yet implemented")
    }
}