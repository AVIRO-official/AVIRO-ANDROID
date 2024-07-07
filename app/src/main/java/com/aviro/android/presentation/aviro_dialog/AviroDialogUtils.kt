package com.aviro.android.presentation.aviro_dialog

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.aviro.android.presentation.home.ui.map.MapViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


object AviroDialogUtils {

    fun createOneDialog(context: Context, title: String?, message : String?, positive_text: String): AlertDialog {

        val builder = MaterialAlertDialogBuilder(ContextThemeWrapper(context, com.aviro.android.R.style.Base_Theme_AVIRO))
        title?.let {
            builder.setTitle(title)
        }
        message?.let {
            builder.setMessage(message)
        }
        builder.setPositiveButton(positive_text) { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(com.aviro.android.R.drawable.base_roundsquare_border_gray7_30)

        return dialog



}
    // 버튼이 2개인 다이얼로그
    fun createTwoDialog(context: Context, title: String, message : String?, positive_text: String, negative_text: String, negativeAction: () -> Unit): AlertDialog {
        val builder = MaterialAlertDialogBuilder(ContextThemeWrapper(context, com.aviro.android.R.style.Base_Theme_AVIRO))

        builder.setTitle(title)
        message?.let {
            builder.setMessage(message)
        }
        builder.setPositiveButton(positive_text) { dialog, which ->
            dialog.dismiss()
        }
        builder.setNegativeButton(negative_text) { dialog, which ->
            Log.d("WithdrawUseCase:createTwoDialog","negative_text")
            negativeAction()
            dialog.dismiss()
        }
        val dialog = builder.create()
        //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setBackgroundDrawableResource(com.aviro.android.R.drawable.base_roundsquare_border_gray7_30)

        return dialog
    }

    // 목록이 있는 다이얼로그
    fun createListDialog(context: Context, viewmodel : MapViewModel): AlertDialog {
        val items = arrayOf("없어진 가게에요", "비건 메뉴가 없는 가게예요", "중복 등록된 가게예요")
        val dialogView = LayoutInflater.from(context).inflate(com.aviro.android.R.layout.restaurant_report_dialog, null)

        val builder = MaterialAlertDialogBuilder(ContextThemeWrapper(context, com.aviro.android.R.style.Base_Theme_AVIRO))
            //.setView(dialogView)
            .setTitle("신고 이유가 궁금해요! 3건 이상의 신고가 들어오면\n가게는 자동으로 삭제돼요.")
            //.setMessage("3건 이상의 신고가 들어오면\n가게는 자동으로 삭제돼요.")
            .setPositiveButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .setItems(items, DialogInterface.OnClickListener { dialogInterface, i ->
            when (i) {
                0 -> {
                    viewmodel.setReportCode(1)
                    dialogInterface.dismiss()
                }
                1 -> {
                    viewmodel.setReportCode(2)
                    dialogInterface.dismiss()
                }
                2 -> {
                    viewmodel.setReportCode(3)
                    dialogInterface.dismiss()
                }

            }
        })

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(com.aviro.android.R.drawable.base_roundsquare_border_gray7_30)


        return dialog
    }

    fun createOneChoiceDialog(context: Context, title: String, itemText : String,
                              itemAction : () -> Unit) : AlertDialog {

        val item = arrayOf(itemText)
        val builder = MaterialAlertDialogBuilder(ContextThemeWrapper(context, com.aviro.android.R.style.Base_Theme_AVIRO))
            .setTitle(title)
            .setPositiveButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .setItems(item , DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    0 -> {
                        itemAction()
                        dialogInterface.dismiss()
                    }

                }
            })

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(com.aviro.android.R.drawable.base_roundsquare_border_gray7_30)

        return dialog

    }

    fun createTwoChoiceDialog(context: Context, title: String, item1Text : String, item2Text : String,
                              item1Action : () -> Unit, item2Action : () -> Unit) : AlertDialog {

        val item = arrayOf(item1Text, item2Text)
        val builder = MaterialAlertDialogBuilder(ContextThemeWrapper(context, com.aviro.android.R.style.Base_Theme_AVIRO))
            .setTitle(title)
            .setPositiveButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .setItems(item , DialogInterface.OnClickListener { dialogInterface, i ->
                when (i) {
                    0 -> {
                        item1Action()
                        dialogInterface.dismiss()
                    }
                    1 -> {
                        item2Action()
                        dialogInterface.dismiss()
                    }
                }
            })

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(com.aviro.android.R.drawable.base_roundsquare_border_gray7_30)

        return dialog


    }


    /*
    fun creatLevelUpPopup(context: Context): AlertDialog {
        val builder = MaterialAlertDialogBuilder(ContextThemeWrapper(context, com.aviro.android.R.style.Base_Theme_AVIRO))

        val inflater = context..layoutInflater
        val dialogView = inflater.inflate(R.layout.levelup_popup, null)
        builder.setView(dialogView)



        return builder.create()
    }

     */



}

