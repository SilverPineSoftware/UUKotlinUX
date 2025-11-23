package com.silverpine.uu.ux

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.silverpine.uu.logging.UULog

private const val LOG_TAG = "UUActivityLauncher"

/**
 * Base class for launching activities and handling their results using the modern Activity Result API.
 *
 * This class provides a type-safe wrapper around [ActivityResultContract] that simplifies
 * the process of launching activities and handling their results. Subclasses implement
 * [handleLaunchResult] to process the result returned from the launched activity.
 *
 * @param Input The input type for the activity result contract (e.g., String for file picker filter)
 * @param Output The output type returned from the activity result contract (e.g., Uri? for file picker)
 * @param contract The [ActivityResultContract] that defines how to launch the activity and parse the result
 *
 * Example implementation:
 * ```kotlin
 * object UUFiles: UUActivityLauncher<String, Uri?>(ActivityResultContracts.GetContent())
 * {
 *     private var completionBlock: ((Uri?)->Unit)? = null
 *
 *     fun requestFile(filter: String, completion: (Uri?) -> Unit)
 *     {
 *         completionBlock = completion
 *         launch(filter)
 *     }
 *
 *     override fun handleLaunchResult(result: Uri?)
 *     {
 *         val block = completionBlock ?: return
 *         completionBlock = null
 *         block(result)
 *     }
 * }
 * ```
 *
 * @see [com.silverpine.uu.ux.files.UUFiles] for an example implementation using file picker
 * @see [com.silverpine.uu.ux.permissions.UUPermissions] for an example implementation using permission requests
 */
abstract class UUActivityLauncher<Input, Output>(
    private val contract: ActivityResultContract<Input, Output>
)
{
    protected var activity: ComponentActivity? = null
    private var launcher:  ActivityResultLauncher<Input>? = null

    /**
     * Initializes the launcher with the given activity.
     *
     * This method must be called before [] can be used. It registers the activity
     * result contract with the activity and sets up the callback mechanism.
     *
     * @param activity The [ComponentActivity] to register the launcher with
     *
     * Example usage:
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?)
     * {
     *     super.onCreate(savedInstanceState)
     *     UUFiles.init(this)
     * }
     * ```
     */
    open fun init(activity: ComponentActivity)
    {
        this.activity = activity
        this.launcher = activity.registerForActivityResult(contract, this::handleLaunchResult)
    }

    /**
     * Called when the launched activity returns a result.
     *
     * This method is invoked automatically by the Activity Result API when the launched
     * activity finishes and returns a result. Subclasses must implement this method to
     * handle the result appropriately.
     *
     * @param result The result returned from the launched activity, or null if the activity
     *               was cancelled or no result was provided
     *
     * Example implementation:
     * ```kotlin
     * override fun handleLaunchResult(result: Uri?)
     * {
     *     if (result != null)
     *     {
     *         // Process the selected file URI
     *         processFile(result)
     *     }
     *     else
     *     {
     *         // User cancelled the file picker
     *         onCancelled()
     *     }
     * }
     * ```
     */
    abstract fun handleLaunchResult(result: Output)

    /**
     * Launches the activity with the given input.
     *
     * This method launches the activity using the registered [ActivityResultContract].
     * The result will be delivered to [handleLaunchResult] when the activity finishes.
     *
     * Note:** [init] must be called before this method can be used. If called before
     * initialization, a warning will be logged and the launch will be ignored.
     *
     * @param input The input parameter for the activity result contract (e.g., MIME type filter for file picker)
     *
     * Example usage:
     * ```kotlin
     * // Launch file picker with image filter
     * UUFiles.launch("image/png") // or image/ *
     *
     * // Launch permission request
     * UUPermissions.launch(arrayOf(
     *     Manifest.permission.CAMERA,
     *     Manifest.permission.ACCESS_FINE_LOCATION
     * ))
     * ```
     */
    fun launch(input: Input)
    {
        if (launcher == null)
        {
            UULog.warn(LOG_TAG, "launcher is null! Call init(activity) before first use.")
        }

        launcher?.launch(input)
    }

    /**
     * Returns the activity that was used to initialize this launcher.
     *
     * This method throws a [RuntimeException] if the launcher has not been initialized
     * via [init]. It is intended for use by subclasses that need access to the activity
     * (e.g., to access SharedPreferences, check permissions, etc.).
     *
     * @return The [ComponentActivity] that was passed to [init]
     * @throws RuntimeException if [init] has not been called
     *
     * Example usage:
     * ```kotlin
     * private val prefs by lazy {
     *     requireActivity().getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE)
     * }
     * ```
     */
    protected fun requireActivity(): ComponentActivity
    {
        val activity = this.activity
        if (activity == null)
        {
            throw RuntimeException("${javaClass.name} not initialized. Must call ${javaClass.name}.init(activity) prior to first use.")
        }

        return activity
    }
}