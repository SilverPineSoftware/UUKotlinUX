package com.silverpine.uu.ux

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.silverpine.uu.core.uuDispatchMain
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.ux.viewmodel.UUFragmentViewModel

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

fun FragmentActivity.uuRemoveFragment(fragment: Fragment, runOnCommit: (()->Unit)? = null)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {
            remove(fragment)

            runOnCommit?.let()
            { block ->
                runOnCommit(block)
            }

            commit()
        }
    }
}

fun FragmentActivity.uuRemoveFragmentByTag(tag: String, runOnCommit: (()->Unit)? = null)
{
    supportFragmentManager.findFragmentByTag(tag)?.let()
    { fragment ->
        uuRemoveFragment(fragment, runOnCommit)
    }
}

fun FragmentActivity.uuReplaceFragment(fragment: Fragment, @IdRes frame: Int, tag: String = fragment.javaClass.name, runOnCommit: (()->Unit)? = null)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {
            replace(frame, fragment, tag)

            runOnCommit?.let()
            { block ->
                runOnCommit(block)
            }

            commit()
        }
    }
}

fun FragmentActivity.uuAddFragment(fragment: Fragment, @IdRes frame: Int, tag: String = fragment.javaClass.name, runOnCommit: (()->Unit)? = null)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {
            add(frame, fragment, tag)

            runOnCommit?.let()
            { block ->
                runOnCommit(block)
            }

            commit()
        }
    }
}

fun FragmentActivity.uuAddOrShowFragment(
    fragment: Fragment,
    @IdRes frame: Int,
    tag: String = fragment.javaClass.name,
    runOnCommit: (()->Unit)? = null)
{
    uuDispatchMain()
    {
        with(supportFragmentManager.beginTransaction())
        {

            supportFragmentManager.findFragmentByTag(tag)?.let()
            { existingFragment ->
                UULog.d(javaClass, "uuAddOrShowFragment", "Fragment $tag is already shown, just showing it.")
                show(existingFragment)
            } ?: run()
            {
                add(frame, fragment, tag)
            }

            runOnCommit?.let()
            { block ->
                runOnCommit(block)
            }

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

const val UU_OVERLAY_FRAGMENT_PREFIX = "uu_overlay_"

fun FragmentActivity.uuReplaceMainFragment(
    fragment: Fragment,
    tag: String = fragment.javaClass.name,
    overlayTagPrefix: String = UU_OVERLAY_FRAGMENT_PREFIX,
    @IdRes mainContainer: Int = R.id.uu_main_frame)
{
    uuDispatchMain()
    {
        val overlayFragments = supportFragmentManager.fragments.filter { it.tag?.startsWith(overlayTagPrefix) ==  true }

        with(supportFragmentManager.beginTransaction())
        {
            replace(mainContainer, fragment, tag)

            overlayFragments.forEach()
            {
                UULog.d(javaClass, "uuReplaceMainFragment", "Removing overlay fragment: $it, tag: ${it.tag}")
                this.remove(it)
            }

            commit()
        }
    }
}

fun FragmentActivity.uuHideOverlayFragment(tag: String, overlayTagPrefix: String = UU_OVERLAY_FRAGMENT_PREFIX, runOnCommit: (()->Unit)? = null)
{
    uuRemoveFragmentByTag("$overlayTagPrefix$tag", runOnCommit)
}

fun Activity.uuContentView(): View?
{
    return try
    {
        findViewById(android.R.id.content)
    }
    catch (ex: Exception)
    {
        UULog.d(javaClass, "uuContentView", "", ex)
        null
    }
}

fun Activity.uuHideSystemUi()
{
    val contentView = uuContentView() ?: return

    // Hide the system UI (top and bottom), as well as enable swiping to reveal them
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, contentView).let()
    { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}















fun FragmentActivity.uuEnableAlias(className: String)
{
    packageManager.setComponentEnabledSetting(
        ComponentName(applicationContext.packageName, className),
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
}

fun FragmentActivity.uuDisableAlias(className: String)
{
    packageManager.setComponentEnabledSetting(
        ComponentName(applicationContext.packageName, className),
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
}

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")
fun Context.uuVibrate(effect: Int)
{
    try
    {
        val vibrator = getSystemService(Vibrator::class.java)
        vibrator.vibrate(VibrationEffect.createPredefined(effect))
    }
    catch (ex: Exception)
    {
        UULog.e(javaClass, "uuVibrate", "", ex)
    }
}

fun FragmentManager.uuMakeTopmostVisibleForAccessibility()
{
    for (f in fragments)
    {
        if (f == fragments.lastOrNull())
        {
            f.view?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            UULog.d(javaClass, "hideOverlayFragment", "Setting fragment ${f.tag} important for accessibility")
        }
        else
        {
            f.view?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
            UULog.d(javaClass, "hideOverlayFragment", "Setting fragment ${f.tag} hidden for accessibility")
        }
    }
}