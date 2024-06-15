package com.aviro.android.presentation.update

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import com.aviro.android.domain.entity.menu.Menu
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.fragment.app.FragmentActivity
import com.aviro.android.R
import com.aviro.android.databinding.ActivityMenuUpdateBinding
import com.aviro.android.databinding.AddMenuLayoutBinding
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.util.HashMap
import java.util.UUID

@AndroidEntryPoint
class UpdateMenu : FragmentActivity() {

    private lateinit var binding : ActivityMenuUpdateBinding
    private val viewmodel : UpdateMenuViewModel by viewModels()

    val menuItemUpdateMap = HashMap<String, Menu>()
    val menuItemNewMap = HashMap<String, Menu>()

    var firstSettingIsRequest = true
    var firstSettingIsVegan = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        initObserver()
        initListener()
        initData()

    }

    fun initData() {
        viewmodel._restaurantInfo.value = intent.getParcelableExtra("RestaurantInfo")
        viewmodel.setVeganType()
        // 메뉴 만큼 뷰 만들어주기
        addOriginMenuItem()
    }

    fun initListener() {
        binding.addMenuBtn.setOnClickListener {
            addNewMenuItem()
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    fun initObserver() {
        viewmodel.toastLiveDate.observe(this) {
            it?.let {successMsg ->
                intent.putExtra("updateSuccess", successMsg)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        viewmodel.errorLiveData.observe(this) {
            it?.let { it -> 
                AviroDialogUtils.createOneDialog(
                    this,
                    "ERROR",
                    "${it}",
                    "확인"
                ).show()
            }
        }
    }

    fun addOriginMenuItem() {
        // 기존 메뉴만큼 추가
        viewmodel._restaurantInfo.value!!.menuArray.forEach { menu ->
            // 데이터 바인딩 인스턴스 생성
            val binding_addMenu: AddMenuLayoutBinding =
                AddMenuLayoutBinding.inflate(layoutInflater)

            var idChecked = false

            // 메뉴, 가격 셋팅
            binding_addMenu.editTextMenu.setText(menu.menu)
            binding_addMenu.editTextPrice.setText(menu.price)

            // 요청사항
            if (menu.menuType == "need to request") {
                binding_addMenu.requestLayout.visibility = View.VISIBLE
                binding_addMenu.requestBox.background = ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                binding_addMenu.editTextRequest.background =
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.base_roundsquare_gray6
                    )
                binding_addMenu.editTextRequest.setText(menu.howToRequest)
                idChecked = true
            } else if(viewmodel.afterVeganTypeList.value!!.get(2)) {
                binding_addMenu.requestBox.background =
                    ContextCompat.getDrawable(this, R.drawable.checkbox_non)
                binding_addMenu.editTextRequest.background =
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.base_roundsquare_gray5
                    )

                binding_addMenu.editTextRequest.setText(menu.howToRequest)
            } else {
                binding_addMenu.requestLayout.visibility = View.GONE
            }

            menuItemUpdateMap[menu.menuId] = menu.copy()
            viewmodel._updateMenuList.value = menuItemUpdateMap

            // 레이아웃을 LinearLayout에 추가
            binding.menuList.addView(binding_addMenu.root)



            // 노랑이 클릭시 요청 editText 보이게
            viewmodel.isRequest.observe(this, androidx.lifecycle.Observer {

                if (it) {
                    binding_addMenu.requestLayout.visibility = View.VISIBLE
                    binding_addMenu.requestBox.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.checkbox_yellow
                    ) //R.drawable.checkbox_yellow
                    idChecked = true

                    menuItemUpdateMap[menu.menuId]?.menuType = "need to request"
                    /*
                    menuItemUpdateMap[menu.menuId]?.let {
                        menuItemUpdateMap[menu.menuId]!!.menuType = "need to request"
                    } ?: run {
                        if(!firstSettingIsRequest && menu.menuType != "need to request") {
                            menuItemUpdateMap[menu.menuId] = menu
                            menuItemUpdateMap[menu.menuId]!!.menuType = "need to request"
                        }
                    }    */

                } else {
                    binding_addMenu.requestLayout.visibility = View.GONE
                    //menuItemUpdateMap[menu.menuId]?.menuType = "vegan"

                    menuItemUpdateMap[menu.menuId]?.menuType = "vegan"
                    menuItemUpdateMap[menu.menuId]?.howToRequest = ""
                    binding_addMenu.editTextRequest.setText("")

                    /*
                    menuItemUpdateMap[menu.menuId]?.let {
                        menuItemUpdateMap[menu.menuId]!!.menuType = "vegan"
                        menuItemUpdateMap[menu.menuId]!!.howToRequest = ""
                        binding_addMenu.editTextRequest.setText("")
                    } ?: run {
                        if(!firstSettingIsRequest && menu.menuType != "vegan") {
                            menuItemUpdateMap[menu.menuId] = menu
                            menuItemUpdateMap[menu.menuId]!!.menuType = "vegan"
                            menuItemUpdateMap[menu.menuId]!!.howToRequest = ""
                            binding_addMenu.editTextRequest.setText("")
                        }


                    }*/

                }
                viewmodel._updateMenuList.value = menuItemUpdateMap
                //firstSettingIsRequest = false
            })

            viewmodel.afterVeganTypeList.observe(this) {
                Log.d("addOriginMenuItem:afterVeganTypeList", "${it}")

                if (it.get(2) == true) {
                    if (it.get(1) == true) {
                        binding_addMenu.requestBox.setOnClickListener { box ->
                            if (idChecked == true) {
                                box.background =
                                    ContextCompat.getDrawable(this, R.drawable.checkbox_non)
                                binding_addMenu.editTextRequest.background =
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.base_roundsquare_gray5
                                    )

                                binding_addMenu.editTextRequest.isEnabled = false

                                menuItemUpdateMap[menu.menuId]?.menuType = "vegan"
                                menuItemUpdateMap[menu.menuId]?.howToRequest = ""
                                binding_addMenu.editTextRequest.setText("")

                                /*
                                menuItemUpdateMap[menu.menuId]?.let {
                                    menuItemUpdateMap[menu.menuId]!!.menuType = "vegan"
                                    menuItemUpdateMap[menu.menuId]!!.howToRequest = ""
                                    binding_addMenu.editTextRequest.setText("")
                                } ?: run {
                                    if(firstSettingIsVegan == false) {
                                        menuItemUpdateMap[menu.menuId] = menu
                                        menuItemUpdateMap[menu.menuId]!!.menuType = "vegan"
                                        menuItemUpdateMap[menu.menuId]!!.howToRequest = ""
                                        binding_addMenu.editTextRequest.setText("")
                                    }


                                }   */



                                viewmodel._updateMenuList.value = menuItemUpdateMap

                            } else {
                                box.background =
                                    ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                                binding_addMenu.editTextRequest.background =
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.base_roundsquare_gray6
                                    )

                                binding_addMenu.editTextRequest.isEnabled = true

                                menuItemUpdateMap[menu.menuId]?.menuType = "need to request"
                                /*
                                menuItemUpdateMap[menu.menuId]?.let {
                                    menuItemUpdateMap[menu.menuId]!!.menuType = "need to request"
                                } ?: run {
                                    menuItemUpdateMap[menu.menuId] = menu
                                    menuItemUpdateMap[menu.menuId]!!.menuType = "need to request"
                                }

                             */

                                viewmodel._updateMenuList.value = menuItemUpdateMap
                            }
                            idChecked = !idChecked
                        }
                    } else { // 요청만 클릭
                        binding_addMenu.requestBox.setOnClickListener(null)
                        binding_addMenu.requestBox.background =
                            ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                        binding_addMenu.editTextRequest.background =
                            ContextCompat.getDrawable(this, R.drawable.base_roundsquare_gray6)

                        binding_addMenu.editTextRequest.isEnabled = true
                        idChecked = true

                        menuItemUpdateMap[menu.menuId]?.menuType = "need to request"

                        /*
                        menuItemUpdateMap[menu.menuId]?.let {
                            menuItemUpdateMap[menu.menuId]!!.menuType = "need to request"
                        } ?: run {
                            if (viewmodel._isFirstSetting.value == false) {
                                menuItemUpdateMap[menu.menuId] = menu
                                menuItemUpdateMap[menu.menuId]!!.menuType = "need to request"
                            }
                     */
                        viewmodel._updateMenuList.value = menuItemUpdateMap
                    }


                    //firstSettingIsVegan = false
                }
            }


                // 메뉴 내용 입력
                binding_addMenu.editTextMenu.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable) {

                        menuItemUpdateMap[menu.menuId]?.let {
                            menuItemUpdateMap[menu.menuId]?.menu = s.toString()
                        } ?: run {
                            menuItemUpdateMap[menu.menuId] = menu
                            menuItemUpdateMap[menu.menuId]?.menu = s.toString()
                        }

                        viewmodel._updateMenuList.value = menuItemUpdateMap

                    }
                })

                binding_addMenu.editTextPrice.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        // 천 단위로 , 찍기
                        val priceFormat = DecimalFormat("#,###")
                        var price = ""

                        binding_addMenu.editTextPrice.removeTextChangedListener(this)
                        if (!TextUtils.isEmpty(s.toString()) && s.toString() != price && s.toString() != "변동가") {
                            price = priceFormat.format(s.toString().replace(",", "").toDouble())
                            binding_addMenu.editTextPrice.setText(price)
                            binding_addMenu.editTextPrice.setSelection(price.length)
                        }

                        //binding_addMenu.editTextPrice.setText(priceFormat.format(price))
                        binding_addMenu.editTextPrice.addTextChangedListener(this)

                        menuItemUpdateMap[menu.menuId]?.let {
                            menuItemUpdateMap[menu.menuId]?.price = price
                        } ?: run {
                            menuItemUpdateMap[menu.menuId] = menu
                            menuItemUpdateMap[menu.menuId]?.price = price
                        }
                        viewmodel._updateMenuList.value = menuItemUpdateMap

                    }
                })


                var isChangeable = true // 변동가 선택 가능

                binding_addMenu.pricePopupMenu.setOnClickListener {
                    val popupMenu = PopupMenu(this, binding_addMenu.pricePopupMenu)
                    popupMenu.inflate(R.menu.register_price_menu_popup)


                    if (isChangeable) {
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

                            menuItemUpdateMap[menu.menuId]?.let {
                                menuItemUpdateMap[menu.menuId]?.price = "변동가"
                            } ?: run {
                                menuItemUpdateMap[menu.menuId] = menu
                                menuItemUpdateMap[menu.menuId]?.price = "변동가"
                            }
                            viewmodel._newMenuList.value = menuItemNewMap

                        } else if (it.getItemId() == R.id.specifiable) {
                            // 변동취소 클릭
                            binding_addMenu.editTextPrice.setText("")
                            binding_addMenu.editTextPrice.isEnabled = true
                            isChangeable = true

                            menuItemUpdateMap[menu.menuId]?.let {
                                menuItemUpdateMap[menu.menuId]?.price = ""
                            } ?: run {
                                menuItemUpdateMap[menu.menuId] = menu
                                menuItemUpdateMap[menu.menuId]?.price = ""
                            }
                            viewmodel._newMenuList.value = menuItemNewMap

                        } else {
                            // 취소 클릭
                            popupMenu.dismiss()
                        }
                        false
                    }
                    popupMenu.show()
                }


                binding_addMenu.editTextRequest.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable) {

                        menuItemUpdateMap[menu.menuId]?.let {
                            menuItemUpdateMap[menu.menuId]?.howToRequest = s.toString()
                        } ?: run {
                            menuItemUpdateMap[menu.menuId] = menu
                            menuItemUpdateMap[menu.menuId]?.howToRequest = s.toString()
                        }
                        viewmodel._updateMenuList.value = menuItemUpdateMap
                    }
                })



            binding_addMenu.cancelMenuBtn.setOnClickListener {
                // 메뉴가 2개 이상이여야 삭제 가능
                if (binding.menuList.size > 1) {
                    viewmodel.deleteMenu(menu.menuId) // 기존 메뉴 -> 삭제
                    binding_addMenu.viewmodel = null

                    binding.menuList.removeView(binding_addMenu.root)
                    if (menu.menuId in menuItemUpdateMap.keys) {
                        //viewmodel._updateMenuList.value!!.remove(menu.menuId)
                        menuItemUpdateMap.remove(menu.menuId)
                        viewmodel._updateMenuList.value = menuItemUpdateMap

                    }
                }
            }

            //viewmodel._isFirstSetting.value = false

        }


        }

        fun addNewMenuItem() {
            // 데이터 바인딩 인스턴스 생성
            val binding_addMenu: AddMenuLayoutBinding =
                AddMenuLayoutBinding.inflate(layoutInflater)

            // 메뉴 추가
            val menuID = UUID.randomUUID().toString()
            val new_menu = Menu(menuID, "vegan", "", "", "", true)
            menuItemNewMap[menuID] = new_menu
            viewmodel._newMenuList.value = menuItemNewMap

            var idChecked = false

            if (viewmodel.afterVeganTypeList.value!!.get(2)) {
                binding_addMenu.requestLayout.visibility = View.VISIBLE
                binding_addMenu.requestBox.background = ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                binding_addMenu.editTextRequest.background =
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.base_roundsquare_gray6
                    )
                idChecked = true
            } else {
                binding_addMenu.requestLayout.visibility = View.GONE
            }

            // 노랑이 클릭시 요청 editText 보이게
            viewmodel.isRequest.observe(this, androidx.lifecycle.Observer {
                if (it) {
                    binding_addMenu.requestLayout.visibility = View.VISIBLE
                    binding_addMenu.requestBox.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.checkbox_yellow
                    ) //R.drawable.checkbox_yellow
                    idChecked = true
                    menuItemNewMap[menuID]?.menuType = "need to request"
                    viewmodel._newMenuList.value = menuItemNewMap
                } else {
                    binding_addMenu.requestLayout.visibility = View.GONE
                    menuItemNewMap[menuID]?.menuType = "vegan"
                    menuItemUpdateMap[menuID]?.howToRequest = ""
                    binding_addMenu.editTextRequest.setText("")
                    viewmodel._newMenuList.value = menuItemNewMap
                }
            })

            viewmodel.afterVeganTypeList.observe(this, androidx.lifecycle.Observer {
                if (it.get(2) == true) {
                    if (it.get(1) == true) {
                        //Log.d("veganTypeList","${it}")

                        binding_addMenu.requestBox.setOnClickListener { box ->
                            if (idChecked == true) {
                                box.background =
                                    ContextCompat.getDrawable(this, R.drawable.checkbox_non)
                                binding_addMenu.editTextRequest.background =
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.base_roundsquare_gray5
                                    )

                                binding_addMenu.editTextRequest.isEnabled = false
                                menuItemNewMap[menuID]?.menuType = "vegan"
                                menuItemUpdateMap[menuID]?.howToRequest = ""
                                binding_addMenu.editTextRequest.setText("")

                                //menuItemUpdateMap[menuID]!!.howToRequest = ""
                                //binding_addMenu.editTextRequest.setText("")
                                viewmodel._newMenuList.value = menuItemNewMap

                            } else {
                                box.background =
                                    ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                                binding_addMenu.editTextRequest.background =
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.base_roundsquare_gray6
                                    )

                                binding_addMenu.editTextRequest.isEnabled = true
                                menuItemNewMap[menuID]!!.menuType = "need to request"
                                viewmodel._newMenuList.value = menuItemNewMap
                            }

                            idChecked = !idChecked
                        }
                    } else {
                        binding_addMenu.requestBox.setOnClickListener(null)
                        binding_addMenu.requestBox.background =
                            ContextCompat.getDrawable(this, R.drawable.checkbox_yellow)
                        binding_addMenu.editTextRequest.background =
                            ContextCompat.getDrawable(this, R.drawable.base_roundsquare_gray6)

                        binding_addMenu.editTextRequest.isEnabled = true
                        idChecked = true
                        menuItemNewMap[menuID]?.menuType = "need to request"
                        viewmodel._newMenuList.value = menuItemNewMap
                    }
                }
            })


            // 메뉴 내용 입력
            binding_addMenu.editTextMenu.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    //Log.d("binding_addMenu","binding_addMenu 생성, ID : ${s.javaClass.name}")
                    menuItemNewMap[menuID]?.menu = s.toString()
                    //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                    viewmodel._newMenuList.value = menuItemNewMap

                }
            })

            // 가격 들어왔을때
            binding_addMenu.editTextPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    // 천 단위로 , 찍기
                    val priceFormat = DecimalFormat("#,###")
                    var price = ""

                    binding_addMenu.editTextPrice.removeTextChangedListener(this)
                    if (!TextUtils.isEmpty(s.toString()) && s.toString() != price && s.toString() != "변동가") {
                        price = priceFormat.format(s.toString().replace(",", "").toDouble())
                        binding_addMenu.editTextPrice.setText(price)
                        binding_addMenu.editTextPrice.setSelection(price.length)
                    }

                    //binding_addMenu.editTextPrice.setText(priceFormat.format(price))
                    binding_addMenu.editTextPrice.addTextChangedListener(this)

                    menuItemNewMap[menuID]?.price = price
                    viewmodel._newMenuList.value = menuItemNewMap
                    //viewmodel._newMenuList.value!![menuID] = viewmodel._newMenuList.value!![menuID]!!.copy(price = price)

                }
            })

            var isChangeable = true // 변동가 선택 가능
            // 변동가, 취소 메뉴
            binding_addMenu.pricePopupMenu.setOnClickListener {
                val popupMenu = PopupMenu(this, binding_addMenu.pricePopupMenu)
                popupMenu.inflate(R.menu.register_price_menu_popup)

                if (isChangeable) {

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

                        menuItemNewMap[menuID]?.price = "변동가"
                        viewmodel._newMenuList.value = menuItemNewMap

                    } else if (it.getItemId() == R.id.specifiable) {
                        // 변동취소 클릭
                        binding_addMenu.editTextPrice.setText("")
                        binding_addMenu.editTextPrice.isEnabled = true
                        isChangeable = true

                        menuItemNewMap[menuID]?.price = ""
                        viewmodel._newMenuList.value = menuItemNewMap

                    } else {
                        // 취소 클릭
                        popupMenu.dismiss()
                    }

                    false
                }
                popupMenu.show()
            }


            binding_addMenu.editTextRequest.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    menuItemNewMap[menuID]?.howToRequest = s.toString()
                    //viewmodel._menuList.value?.get(menuID)?.menu = s.toString()
                    viewmodel._newMenuList.value = menuItemNewMap

                }
            })

            // 레이아웃을 LinearLayout에 추가
            binding.menuList.addView(binding_addMenu.root)

            binding_addMenu.cancelMenuBtn.setOnClickListener {
                if (binding.menuList.size > 1) {
                    menuItemNewMap.remove(menuID)
                    binding_addMenu.viewmodel = null
                    binding.menuList.removeView(binding_addMenu.root)

                    menuItemNewMap.remove(menuID)
                    viewmodel._newMenuList.value = menuItemNewMap


                }
            }

        }
}