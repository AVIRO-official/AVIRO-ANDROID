package com.aviro.android.presentation.home.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.aviro.android.R
import com.aviro.android.domain.entity.menu.Menu
import com.aviro.android.databinding.*
import com.aviro.android.domain.entity.search.SearchedRestaurantItem
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.aviro_dialog.LevelUpPopUp
import com.aviro.android.presentation.home.Home
import com.aviro.android.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.util.*

@AndroidEntryPoint
class RegisterFragment : FragmentActivity() { // Fragment()

    /*
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
     */

    private lateinit var binding: FragmentRegisterBinding

    private val viewmodel: RegisterViewModel by viewModels()
    //private val homeViewmodel: HomeViewModel by activityViewModels()

    val menuItemMap = HashMap<String, Menu>()
    var location_x : Double? = null
    var location_y : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        addMenuItem()
        initListener()
        initObserver()

    }

    fun initListener() {
        // 가게 찾기

        binding.searchbar.setOnClickListener {
            val intent = Intent(this, SearchRegisteration::class.java)
            // 현재 지도의 중심 위치 정보 전다해줘야 함
            val currentUserLoc = getCurrentGPSLoc()
            currentUserLoc?.let {
                // 사용자 위치 기준
                Log.d("searchbarClick, 사용","${currentUserLoc}")
                intent.putExtra("NaverMapOfX", currentUserLoc.longitude)
                intent.putExtra("NaverMapOfY", currentUserLoc.latitude)

            } ?: run {
                // 맵 중심점  기준
                Log.d("searchbarClick","${location_x}, ${location_y}")
                intent.putExtra("NaverMapOfX", location_x)
                intent.putExtra("NaverMapOfY", location_y)
            }

            startActivityForResult(intent, getString(R.string.SEARCH_RESULT_OK).toInt())
        }



        // 메뉴 추가
        binding.addMenuBtn.setOnClickListener {
            addMenuItem()
        }

        binding.backBtn.setOnClickListener {
            // 정말 취소할건지 물어보는 팝업창
            AviroDialogUtils.createTwoDialog(this,
                "정말로 가게 등록을 취소하나요?",
                "작성하던 가게 등록 정보가 모두 삭제됩니다.",
                "아니요",
                "예",
                {
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                    //overridePendingTransition(0, 0)
                }).show()
        }

        /*
        binding.scroll.setOnTouchListener(object: OnSwipeTouchListener(context = this) {
            override fun onSwipeLeft() {}
            override fun onSwipeRight() {
                // 뷰페이저 슬라이드 가능
                val viewPager = this.findViewById<ViewPager2>(R.id.home_pager)
                var current = viewPager.currentItem
                if (current == 1){
                    //binding.scrollLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_left))
                    //overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
                    viewPager.setCurrentItem(0, true)
                }
            }
        })

         */

    }

    fun initObserver() {
        viewmodel.levelUp.observe(this) {
            // 레벨업 팝업 띄우기
            if(it.levelUp) {
                intent.putExtra("levelUp", it)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
            //overridePendingTransition(0, 0)

        }

        viewmodel.errorLiveData.observe(this) {
            intent.putExtra("error", it)
            setResult(Activity.RESULT_OK, intent)
            finish()
            //overridePendingTransition(0, 0)
        }
    }

    fun getCurrentGPSLoc(): Location? {
        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 위치 기준
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) //manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else {
                // 위치 권한이 없는 경우 요청하도록 구현
                // 혹은 그냥 현재 맵의 중심점을 반환
                return null
            }
        }
        // 맵 기준
        return null
    }



    fun addMenuItem() {
        // 데이터 바인딩 인스턴스 생성
        val binding_addMenu: AddMenuLayoutBinding =
            AddMenuLayoutBinding.inflate(layoutInflater)


        // 메뉴 추가
        val menuID = UUID.randomUUID().toString()
        val new_menu = Menu(menuID,"vegan","","","",true)
        menuItemMap[menuID] = new_menu
        viewmodel._menuList.value = menuItemMap

        var isChecked = false

        // 노랑이 클릭시 요청 editText 보이게
        viewmodel.isRequest.observe(this) {
            if (it) {
                binding_addMenu.requestLayout.visibility = View.VISIBLE
                binding_addMenu.requestBox.background =
                    ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)

                binding_addMenu.editTextRequest.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.base_roundsquare_gray6
                )
                isChecked = true
                menuItemMap[menuID]?.menuType = "need to request"
                viewmodel._menuList.value = menuItemMap
            } else {
                binding_addMenu.requestLayout.visibility = View.GONE
                menuItemMap[menuID]?.menuType = "vegan"
                viewmodel._menuList.value = menuItemMap
            }
        }

        viewmodel.veganTypeList.observe(this) {
            if(it.get(2) == true) {
                if (it.get(1) == true) {
                    //Log.d("veganTypeList","${it}")
                    binding_addMenu.requestBox.setOnClickListener { box ->
                        if (isChecked == true) {
                            box.background =
                                ContextCompat.getDrawable(this, R.drawable.checkbox_non)
                            binding_addMenu.editTextRequest.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.base_roundsquare_gray5
                            )
                            binding_addMenu.editTextRequest.isEnabled = false
                            menuItemMap[menuID]?.menuType = "vegan"
                            menuItemMap[menuID]?.howToRequest = ""
                            binding_addMenu.editTextRequest.setText("")
                            viewmodel._menuList.value = menuItemMap

                        } else {
                            box.background =
                                ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                            binding_addMenu.editTextRequest.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.base_roundsquare_gray6
                            )
                            binding_addMenu.editTextRequest.setHintTextColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.Gray3
                                )
                            )
                            binding_addMenu.editTextRequest.isEnabled = true
                            menuItemMap[menuID]?.menuType = "need to request"
                            viewmodel._menuList.value = menuItemMap
                        }

                        isChecked = !isChecked
                    }
                } else { // 오렌지(일부비건) 선택X, 요청시 비건

                    binding_addMenu.requestBox.setOnClickListener(null)
                    binding_addMenu.requestBox.background =
                        ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                    binding_addMenu.editTextRequest.background =
                        ContextCompat.getDrawable(this, R.drawable.base_roundsquare_gray6)
                    binding_addMenu.editTextRequest.setHintTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.Gray3
                        )
                    )
                    binding_addMenu.editTextRequest.isEnabled = true
                    isChecked = true
                    menuItemMap[menuID]?.menuType = "need to request"
                    viewmodel._menuList.value = menuItemMap

                }
            }
        }


        // 메뉴 내용 입력
        binding_addMenu.editTextMenu.addTextChangedListener( object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s : Editable) {
                //Log.d("binding_addMenu","binding_addMenu 생성, ID : ${s.javaClass.name}")
                menuItemMap[menuID]!!.menu = s.toString()
                //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                viewmodel._menuList.value = menuItemMap
            }
        })

        binding_addMenu.editTextPrice.addTextChangedListener( object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s : Editable) {
                // 천 단위로 , 찍기
                val priceFormat = DecimalFormat("#,###")
                var price = ""

                binding_addMenu.editTextPrice.removeTextChangedListener(this)
                if(!TextUtils.isEmpty(s.toString()) && s.toString() != price && s.toString() != "변동가"){
                    price = priceFormat.format(s.toString().replace(",","").toDouble())
                    binding_addMenu.editTextPrice.setText(price)
                    binding_addMenu.editTextPrice.setSelection(price.length)
                }

                //binding_addMenu.editTextPrice.setText(priceFormat.format(price))
                binding_addMenu.editTextPrice.addTextChangedListener(this)

                menuItemMap[menuID]!!.price = price
                viewmodel._menuList.value = menuItemMap

            }
        })


        var isChangeable = true
        binding_addMenu.pricePopupMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, binding_addMenu.pricePopupMenu)
            popupMenu.inflate(R.menu.register_price_menu_popup)

            if(isChangeable) {
                val item_specify = popupMenu.menu.findItem(R.id.specifiable)
                item_specify.isVisible = false
                val item_change = popupMenu.menu.findItem(R.id.changeable)
                item_change.isVisible = true
            } else {
                val item_change = popupMenu.menu.findItem(R.id.changeable)
                item_change.isVisible = false
                val item_specify = popupMenu.menu.findItem(R.id.specifiable)
                item_specify.isVisible = true
            }

            popupMenu.setOnMenuItemClickListener { it ->

                if (it.getItemId() == R.id.changeable) {
                    // 변동가 클릭
                    binding_addMenu.editTextPrice.setText("변동가")
                    binding_addMenu.editTextPrice.isEnabled = false
                    isChangeable = false

                    menuItemMap[menuID]!!.price = "변동가"
                    viewmodel._menuList.value = menuItemMap

                } else if(it.getItemId() == R.id.specifiable) {
                    // 변동취소 클릭
                    binding_addMenu.editTextPrice.setText("")
                    binding_addMenu.editTextPrice.isEnabled = true
                    isChangeable = true

                    menuItemMap[menuID]!!.price = ""
                    viewmodel._menuList.value = menuItemMap

                } else {
                    // 취소 클릭
                    popupMenu.dismiss()
                }

                false
            }
            popupMenu.show()
        }

        binding_addMenu.editTextRequest.addTextChangedListener( object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s : Editable) {
                menuItemMap[menuID]!!.howToRequest = s.toString()
                //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                viewmodel._menuList.value = menuItemMap

            }
        })

        binding_addMenu.cancelMenuBtn.setOnClickListener {
            // 메뉴 1개는 남김
            if(binding.menuList.size > 1) {
                menuItemMap.remove(menuID)
                binding_addMenu.viewmodel = null
                binding.menuList.removeView(binding_addMenu.root)
                viewmodel._menuList.value!!.remove(menuID)
            }
        }

        // 레이아웃을 LinearLayout에 추가
        binding.menuList.addView(binding_addMenu.root)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 요청 코드가 일치하고 결과 코드가 성공인 경우
        /*
        if (requestCode == getString(R.string.SEARCH_RESULT_OK).toInt() && resultCode == Activity.RESULT_OK) {
            val resultData = data?.getParcelableExtra<SearchedRestaurantItem>("search_item")

        }

         */

        when (requestCode) {
            getString(R.string.SEARCH_RESULT_OK).toInt() -> {
                if(data != null){
                    val serahed_item = data.getParcelableExtra<SearchedRestaurantItem>("search_item")
                    serahed_item?.let {
                        // 가게 정보 표시
                        viewmodel._registerRestaurant.value = it
                        binding.searchbarTextView.text = it.placeName
                        }
                }
            }
        }
    }

    override fun onBackPressed() {
        if(false) {
            super.onBackPressed()
        } else {
            // 정말 취소할건지 물어보는 팝업창
            AviroDialogUtils.createTwoDialog(this,
                "정말로 가게 등록을 취소하나요?",
                "작성하던 가게 등록 정보가 모두 삭제됩니다.",
                "아니요",
                "예",
                {
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                    //overridePendingTransition(0, 0)
                }).show()
        }

    }

}
/*
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

 */


