package com.android.aviro.presentation.home.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.R
import com.android.aviro.databinding.*
import com.android.aviro.presentation.entity.MenuEntity
import com.android.aviro.presentation.home.Home
import com.android.aviro.presentation.home.ui.mypage.MypageViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import kotlin.collections.HashMap


class RegisterFragment : Fragment()  {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: RegisterViewModel by viewModels()//(R.id.navigation_register)

    //private lateinit var mGestureDetector : GestureDetector

    val menuItemMap = HashMap<String, MenuEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        addMenuItem()

        binding.scroll.setOnTouchListener(object: OnSwipeTouchListener(context = requireContext()) {
            override fun onSwipeLeft() {
                Log.d("Swipe","onLeft")
            }
            override fun onSwipeRight() {
                Log.d("Swipe","onRight")
                // 뷰페이저 슬라이드 가능
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.home_pager)
                var current = viewPager.currentItem
                if (current == 1){
                    //binding.scrollLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_left))
                    //overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
                    viewPager.setCurrentItem(0, true)
                }
            }
        })

        //val adapter1 = ArrayAdapter(requireContext(), R.layout.add_menu_layout)
        //binding.menuListView.adapter = adapter1

        // 메뉴 추가
        binding.aadMenuBtn.setOnClickListener {
            addMenuItem()
        }


        binding.backBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.home_pager)
            viewPager.setCurrentItem(0, true)
        }


        return root


    }

    fun addMenuItem() {
        // 데이터 바인딩 인스턴스 생성
        val binding_addMenu: AddMenuLayoutBinding =
            AddMenuLayoutBinding.inflate(layoutInflater)

        // 바인딩 클래스에 뷰모델 설정
        //binding_addMenu.viewmodel = this.viewmodel
        //binding_addMenu.lifecycleOwner = this

        // 메뉴 추가
        val menuID = UUID.randomUUID().toString()
        val new_menu = MenuEntity(menuID,"vegan","","","",true)
        menuItemMap[menuID] = new_menu
        viewmodel._menuList.value = menuItemMap

        var idChecked = false

        // 노랑이 클릭시 요청 editText 보이게
        viewmodel.isRequest.observe(this, androidx.lifecycle.Observer {
          if(it) {
              binding_addMenu.requestLayout.visibility = View.VISIBLE
              binding_addMenu.requestBox.background = ContextCompat.getDrawable(context!!, R.drawable.checkbox_yellow) //R.drawable.checkbox_yellow
              idChecked = true
              menuItemMap[menuID]!!.menuType = "need to request"
              viewmodel._menuList.value = menuItemMap
          } else {
              binding_addMenu.requestLayout.visibility = View.GONE
              menuItemMap[menuID]!!.menuType = "vegan"
              viewmodel._menuList.value = menuItemMap
          }
        })

        viewmodel.veganTypeList.observe(this, androidx.lifecycle.Observer {

            if(it.get(2) == true) {
                if (it.get(1) == true) {
                    //Log.d("veganTypeList","${it}")
                    binding_addMenu.requestBox.setOnClickListener { box ->
                        if (idChecked == true) {
                            box.background =
                                ContextCompat.getDrawable(context!!, R.drawable.checkbox_non)
                            binding_addMenu.editTextRequest.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.base_roundsquare_gray5
                            )
                            binding_addMenu.editTextRequest.setHintTextColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.Gray0
                                )
                            )
                            binding_addMenu.editTextRequest.isEnabled = false
                            menuItemMap[menuID]!!.menuType = "vegan"
                            viewmodel._menuList.value = menuItemMap

                        } else {
                            box.background =
                                ContextCompat.getDrawable(context!!, R.drawable.checkbox_yellow)
                            binding_addMenu.editTextRequest.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.base_roundsquare_gray6
                            )
                            binding_addMenu.editTextRequest.setHintTextColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.Gray3
                                )
                            )
                            binding_addMenu.editTextRequest.isEnabled = true
                            menuItemMap[menuID]!!.menuType = "need to request"
                            viewmodel._menuList.value = menuItemMap

                        }

                        idChecked = !idChecked
                    }
                } else {
                    binding_addMenu.requestBox.setOnClickListener(null)
                    binding_addMenu.requestBox.background =
                        ContextCompat.getDrawable(context!!, R.drawable.checkbox_yellow)
                    binding_addMenu.editTextRequest.background =
                        ContextCompat.getDrawable(context!!, R.drawable.base_roundsquare_gray6)
                    binding_addMenu.editTextRequest.setHintTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.Gray3
                        )
                    )
                    binding_addMenu.editTextRequest.isEnabled = true
                    idChecked = true
                    menuItemMap[menuID]!!.menuType = "need to request"
                    viewmodel._menuList.value = menuItemMap
                    /*
                    binding_addMenu.requestBox.setOnClickListener { box ->

                    }

                     */
                }
            }
        })


        // 메뉴 내용 입력
        binding_addMenu.editTextMenu.addTextChangedListener( object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("beforeTextChanged","beforeTextChanged : ${p0}")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("onTextChanged","onTextChanged : ${p0}")
            }
            override fun afterTextChanged(s : Editable) {
                //Log.d("binding_addMenu","binding_addMenu 생성, ID : ${s.javaClass.name}")
                menuItemMap[menuID]!!.menu = s.toString()
                //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                viewmodel._menuList.value = menuItemMap

            }
        })

        binding_addMenu.editTextPrice.addTextChangedListener( object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("beforeTextChanged","beforeTextChanged : ${p0}")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("onTextChanged","onTextChanged : ${p0}")
            }
            override fun afterTextChanged(s : Editable) {
                //Log.d("binding_addMenu","binding_addMenu 생성, ID : ${s.javaClass.name}")
                menuItemMap[menuID]!!.price = s.toString()
                //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                viewmodel._menuList.value = menuItemMap

            }
        })


        binding_addMenu.editTextRequest.addTextChangedListener( object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("beforeTextChanged","beforeTextChanged : ${p0}")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("onTextChanged","onTextChanged : ${p0}")
            }
            override fun afterTextChanged(s : Editable) {
                //Log.d("binding_addMenu","binding_addMenu 생성, ID : ${s.javaClass.name}")
                menuItemMap[menuID]!!.howToRequest = s.toString()
                //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                viewmodel._menuList.value = menuItemMap

            }
        })

        // 레이아웃을 LinearLayout에 추가
        binding.menuList.addView(binding_addMenu.root)

        binding_addMenu.cancelMenuBtn.setOnClickListener {
            Log.d("binding_addMenu","binding_addMenu 삭제")
            if(binding.menuList.size > 1) {
                binding_addMenu.viewmodel = null
                binding.menuList.removeView(binding_addMenu.root)
                viewmodel._menuList.value!!.remove(menuID)
            }

        }

    }

    fun back() {
        //this.onBackPressed()
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
            if (e1 != null) {
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
            }
            return false
        }
    }
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}


