package com.silverpine.uu.ux.permissions

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.logging.logException
import com.silverpine.uu.ux.UUActivityLauncher
import java.io.File
import java.util.Properties

private const val LOG_TAG = "UUPermissions"
private const val PREFS_FILE_NAME = "uu_permissions_flags.properties"

object UUPermissions:
    UUActivityLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>(ActivityResultContracts.RequestMultiplePermissions()),
    UUPermissionProvider
{
    private fun getPrefsFile(): File? = runCatching()
    {
        File(requireActivity().noBackupFilesDir, PREFS_FILE_NAME)
    }.getOrElse()
    { ex ->
        UULog.logException(LOG_TAG, "getPrefsFile", ex)
        null
    }
    
    @Volatile
    private var cachedPrefs: Properties? = null
    private var cachedPrefsFile: File? = null
    
    private fun getPrefs(): Properties
    {
        val currentFile = getPrefsFile() ?: return Properties()

        // Reload if file changed or not yet loaded
        if (cachedPrefs == null || cachedPrefsFile != currentFile)
        {
            cachedPrefs = loadPrefs(currentFile)
            cachedPrefsFile = currentFile
        }

        return cachedPrefs ?: Properties()
    }
    
    private fun loadPrefs(prefsFile: File): Properties
    {
        val props = Properties()
        if (prefsFile.exists())
        {
            try
            {
                prefsFile.inputStream().use { props.load(it) }
            }
            catch (ex: Exception)
            {
                UULog.logException(LOG_TAG, "loadPrefs", ex)
            }
        }
        return props
    }
    
    private fun savePrefs()
    {
        try
        {
            val prefsFile = getPrefsFile() ?: return
            val currentPrefs = getPrefs()
            prefsFile.outputStream().use { currentPrefs.store(it, null) }
        }
        catch (ex: Exception)
        {
            UULog.logException(LOG_TAG, "savePrefs", ex)
        }
    }

    private var completionBlock: ((Map<String, UUPermissionStatus>) -> Unit)? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UUActivityLauncher
    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun handleLaunchResult(result: Map<String, @JvmSuppressWildcards Boolean>)
    {
        val statusResults: MutableMap<String, UUPermissionStatus> = mutableMapOf()

        result.forEach()
        { permission, granted ->
            setHasRequestedPermission(permission)
            statusResults[permission] = if (granted) UUPermissionStatus.GRANTED else deniedStatus(permission)
        }

        val block = completionBlock ?: return
        completionBlock = null
        block(statusResults)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UUPermissionProvider
    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun getPermissionStatus(permission: String): UUPermissionStatus = runCatching()
    {
        // Check if permission has ever been requested
        val hasEverRequested = hasEverRequestedPermission(permission).getOrElse { return@runCatching UUPermissionStatus.NEVER_ASKED }
        
        if (!hasEverRequested)
        {
            UUPermissionStatus.NEVER_ASKED
        }
        else
        {
            // Check if permission is currently granted
            val isGranted = hasPermission(permission).getOrElse { return@runCatching UUPermissionStatus.DENIED }
            
            if (isGranted)
            {
                UUPermissionStatus.GRANTED
            }
            else
            {
                deniedStatus(permission)
            }
        }
    }.getOrDefault(UUPermissionStatus.UNDETERMINED)

    override fun requestPermissions(permissions: Array<String>, completion: (Map<String, UUPermissionStatus>) -> Unit)
    {
        val permissionStatuses = getPermissionStatusMultiple(permissions)

        // If all permissions are granted already, return success immediately
        if (permissionStatuses.all { it.value.isGranted })
        {
            completion(permissionStatuses)
            return
        }

        completionBlock = completion
        launch(permissions)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Private Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun prefKey(permission: String): String
    {
        return "UU_HAS_REQUESTED_$permission"
    }

    private fun hasEverRequestedPermission(permission: String): Result<Boolean> = runCatching()
    {
        val currentPrefs = getPrefs()
        val value = currentPrefs.getProperty(prefKey(permission), "false")
        value.toBoolean()
    }

    private fun setHasRequestedPermission(permission: String)
    {
        try
        {
            val currentPrefs = getPrefs()
            currentPrefs.setProperty(prefKey(permission), "true")
            savePrefs()
        }
        catch (ex: Exception)
        {
            UULog.logException(LOG_TAG, "setHasRequestedPermission", ex)
        }
    }

    private fun hasPermission(permission: String): Result<Boolean> = runCatching()
    {
        (ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun canRequestPermission(permission: String): Result<Boolean> = runCatching()
    {
        !hasEverRequestedPermission(permission).getOrThrow() || shouldShowRationale(permission).getOrThrow()
    }

    private fun shouldShowRationale(permission: String): Result<Boolean> = runCatching()
    {
        requireActivity().shouldShowRequestPermissionRationale(permission)
    }

    private fun deniedStatus(permission: String): UUPermissionStatus
    {
        val canRequest = canRequestPermission(permission).getOrElse { return UUPermissionStatus.DENIED }

        return if (canRequest)
        {
            UUPermissionStatus.DENIED
        }
        else
        {
            UUPermissionStatus.PERMANENTLY_DENIED
        }
    }
}