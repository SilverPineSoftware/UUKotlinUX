package com.silverpine.uu.ux.viewmodel

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.silverpine.uu.ux.UUAlertDialog
import com.silverpine.uu.ux.UUToast
import com.silverpine.uu.ux.uuHideKeyboard
import com.silverpine.uu.ux.uuOpenSystemSettings
import com.silverpine.uu.ux.uuOpenUrl
import com.silverpine.uu.ux.uuShowAlertDialog
import com.silverpine.uu.ux.uuShowToast
import com.silverpine.uu.ux.uuStartActivity
import com.silverpine.uu.ux.uuVibrate

open class UUViewModel: ViewModel()
{
    var uuHideKeyboard: ()->Unit = { }
    var uuShowAlertDialog: (UUAlertDialog)->Unit = { }
    var uuStartActivity: (Class<out AppCompatActivity>, Bundle?)->Unit = { _, _ -> }
    var uuOpenUrl: (String)->Unit = { }
    var uuVibrate: (Int)->Unit = { }
    var uuToast: (UUToast)->Unit = { }
    var uuOpenSystemSettings: ()->Unit = { }
}

fun Activity.uuSetupViewModel(viewModel: UUViewModel)
{
    viewModel.uuHideKeyboard = this::uuHideKeyboard
    viewModel.uuOpenUrl = this::uuOpenUrl

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
    {
        viewModel.uuVibrate = this::uuVibrate
    }

    viewModel.uuToast = this::uuShowToast
    viewModel.uuOpenSystemSettings = this::uuOpenSystemSettings
    viewModel.uuShowAlertDialog = this::uuShowAlertDialog
    viewModel.uuStartActivity = this::uuStartActivity
}