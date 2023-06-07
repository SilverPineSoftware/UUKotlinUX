package com.silverpine.uu.ux

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.silverpine.uu.logging.UULog

typealias UUSinglePermissionDelegate = (String,Boolean)->Unit
typealias UUMultiplePermissionDelegate = (HashMap<String,Boolean>)->Unit

object UUPermissions
{
    private val callbacks = HashMap<Int, UUMultiplePermissionDelegate>()

    fun hasPermission(context: Context?, permission: String?): Boolean
    {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context!!,
            permission!!
        )
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    fun hasAllPermissions(context: Context, permissions: Array<String>): Boolean
    {
        for (permission in permissions)
        {
            if (!hasPermission(context, permission))
            {
                return false
            }
        }

        return true
    }

    private fun hasEverRequestedPermission(context: Context, permission: String): Boolean
    {
        val prefs = context.getSharedPreferences(UUPermissions::class.java.name, Activity.MODE_PRIVATE)
        return prefs.getBoolean("HAS_REQUESTED_$permission", false)
    }

    private fun setHasRequestedPermission(context: Context, permission: String)
    {
        val prefs = context.getSharedPreferences(UUPermissions::class.java.name, Activity.MODE_PRIVATE)
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean("HAS_REQUESTED_$permission", true)
        prefsEditor.apply()
    }

    fun canRequestPermission(activity: Activity, permission: String): Boolean
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                return !hasEverRequestedPermission(activity, permission) || activity.shouldShowRequestPermissionRationale(permission)
            }
        }
        catch (ex: Exception)
        {
            UULog.e(javaClass, "canRequestPermission", "", ex)
        }

        return true
    }

    fun canRequestAllPermissions(activity: Activity, permissions: Array<String>): Boolean
    {
        var canRequestAll = true
        for (p in permissions)
        {
            if (!canRequestPermission(activity, p))
            {
                canRequestAll = false
                break
            }
        }

        return canRequestAll
    }

    fun requestPermissions(
        activity: Activity,
        permission: String,
        requestId: Int,
        delegate: UUSinglePermissionDelegate
    )
    {
        requestMultiplePermissions(activity, arrayOf(permission), requestId)
        { results ->
            var granted = false
            if (results != null) {
                val result = results[permission]
                if (result != null) {
                    granted = result
                }
            }
            safeNotifyDelegate(delegate, permission, granted)
        }
    }

    fun requestMultiplePermissions(
        activity: Activity,
        permissions: Array<String>,
        requestId: Int,
        delegate: UUMultiplePermissionDelegate
    ) {
        if (hasAllPermissions(activity, permissions)) {
            val results = HashMap<String, Boolean>()
            for (p in permissions) {
                results[p] = java.lang.Boolean.TRUE
            }
            safeNotifyDelegate(delegate, results)
            removeDelegate(requestId)
        } else {
            // Wait for the results
            saveDelegate(delegate, requestId)
            ActivityCompat.requestPermissions(activity!!, permissions, requestId)
        }
    }

    fun handleRequestPermissionsResult(
        activity: Activity,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        try {
            var delegate: UUMultiplePermissionDelegate? = null
            if (callbacks.containsKey(requestCode)) {
                delegate = callbacks[requestCode]
                if (permissions.size == grantResults.size) {
                    val results = HashMap<String, Boolean>()
                    for (i in permissions.indices) {
                        val permission = permissions[i]
                        setHasRequestedPermission(activity, permission)
                        val result = grantResults[i]
                        results[permission] = result == PackageManager.PERMISSION_GRANTED
                    }
                    safeNotifyDelegate(delegate, results)
                }
                removeDelegate(requestCode)
                return true
            }
        } catch (ex: Exception) {
            UULog.e(javaClass, "handleRequestPermissionsResult", "", ex)
        }
        return false
    }

    @Synchronized
    private fun saveDelegate(delegate: UUMultiplePermissionDelegate?, requestId: Int) {
        try {
            if (delegate != null) {
                callbacks[requestId] = delegate
            }
        } catch (ex: Exception) {
            UULog.e(javaClass, "saveDelegate", "", ex)
        }
    }

    @Synchronized
    private fun removeDelegate(requestId: Int) {
        try {
            if (callbacks.containsKey(requestId)) {
                callbacks.remove(requestId)
            }
        } catch (ex: Exception) {
            UULog.e(javaClass, "safeNotifyDelegate", "", ex)
        }
    }

    private fun safeNotifyDelegate(
        delegate: UUSinglePermissionDelegate?,
        permission: String,
        granted: Boolean
    ) {
        try {
            delegate?.invoke(permission, granted)
        } catch (ex: Exception) {
            UULog.e(javaClass, "safeNotifyDelegate", "", ex)
        }
    }

    private fun safeNotifyDelegate(
        delegate: UUMultiplePermissionDelegate?,
        results: HashMap<String, Boolean>
    ) {
        try {
            delegate?.invoke(results)
        } catch (ex: Exception) {
            UULog.e(javaClass, "safeNotifyDelegate", "", ex)
        }
    }
}