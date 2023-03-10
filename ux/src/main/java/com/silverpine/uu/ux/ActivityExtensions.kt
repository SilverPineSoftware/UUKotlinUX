package com.silverpine.uu.ux

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull

fun Activity.uuOpenUrl(@NonNull url: String)
{
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}

fun Activity.uuCallPhoneNumber(@NonNull phoneNumber: String)
{
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
    startActivity(intent)
}

fun Activity.uuOpenSystemSettings()
{
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Activity.uuOpenEmail(@NonNull email: String)
{
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse("mailto: $email")
    startActivity(Intent.createChooser(emailIntent, title))
}

fun Activity.uuHideKeyboard()
{
    currentFocus?.let()
    { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}
