package com.silverpine.uu.ux

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.silverpine.uu.core.uuDispatchMain

fun Activity.uuStartActivity(activityClass: Class<out Activity>, args: Bundle?)
{
    uuDispatchMain()
    {
        val intent = Intent(applicationContext, activityClass)
        args?.let { intent.putExtras(it) }
        startActivity(intent)
    }
}

fun Activity.uuOpenUrl(url: String)
{
    uuDispatchMain()
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}

fun Activity.uuCallPhoneNumber(phoneNumber: String)
{
    uuDispatchMain()
    {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
        startActivity(intent)
    }
}

fun Activity.uuOpenSystemSettings()
{
    uuDispatchMain()
    {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}

fun Activity.uuOpenEmail(email: String)
{
    uuDispatchMain()
    {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto: $email")
        startActivity(Intent.createChooser(emailIntent, title))
    }
}

fun Activity.uuHideKeyboard()
{
    uuDispatchMain()
    {
        currentFocus?.let()
        { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}

//appId -> package name
fun Activity.uuOpenAppInGooglePlay(appId: String)
{
    uuDispatchMain()
    {
        try
        {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")))
        }
        catch (ex: ActivityNotFoundException)
        {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appId")))
        }
    }
}

fun FragmentActivity.uuRemoveFragment(fragment: Fragment)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {
            remove(fragment)
            commit()
        }
    }
}

fun FragmentActivity.uuRemoveFragmentByTag(tag: String)
{
    supportFragmentManager.findFragmentByTag(tag)?.let()
    { fragment ->
        uuRemoveFragment(fragment)
    }
}

fun FragmentActivity.uuReplaceFragment(fragment: Fragment, @IdRes frame: Int, tag: String = fragment.javaClass.name)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {
            replace(frame, fragment, tag)
            commit()
        }
    }
}

fun FragmentActivity.uuAddFragment(fragment: Fragment, @IdRes frame: Int, tag: String = fragment.javaClass.name)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {
            add(frame, fragment, tag)
            commit()
        }
    }
}

fun FragmentActivity.uuShowDialogFragment(fragment: DialogFragment, tag: String)
{
    uuDispatchMain()
    {
        uuRemoveFragmentByTag(tag)
        fragment.show(supportFragmentManager, tag)
    }
}
