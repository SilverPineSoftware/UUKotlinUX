package com.silverpine.uu.ux.files

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.silverpine.uu.ux.UUActivityLauncher

/**
 * Default implementation of [UUFileProvider] for requesting files from the user.
 *
 * This object provides a singleton implementation that uses the Android Activity Result API
 * to launch the system file picker and handle the selected file URI. It extends [UUActivityLauncher]
 * with [ActivityResultContracts.GetContent] to handle the file selection flow.
 *
 * **Initialization:**
 * This object must be initialized with a [androidx.activity.ComponentActivity] before use:
 * ```kotlin
 * override fun onCreate(savedInstanceState: Bundle?)
 * {
 *     super.onCreate(savedInstanceState)
 *     UUFiles.init(this)
 * }
 * ```
 *
 * **Usage:**
 * After initialization, use [requestFile] to launch the file picker:
 * ```kotlin
 * // Request an image file
 * UUFiles.requestFile("image" + "/" + "*") { context, uri ->
 *     uri?.let {
 *         // User selected a file - process the URI
 *         // context provides access to application context for file operations
 *         loadImageFromUri(context, it)
 *     } ?: run {
 *         // User cancelled the file picker
 *         showMessage("File selection cancelled")
 *     }
 * }
 *
 * // Request a PDF file
 * UUFiles.requestFile("application/pdf") { context, uri ->
 *     uri?.let {
 *         openPdf(context, it)
 *     }
 * }
 *
 * // Request any file type
 * UUFiles.requestFile("*" + "/" + "*") { context, uri ->
 *     uri?.let {
 *         processFile(context, it)
 *     }
 * }
 * ```
 *
 * @see [UUFileProvider] for the interface definition
 * @see [UUActivityLauncher] for the base class implementation
 */
object UUFiles:
    UUActivityLauncher<String, Uri?>(ActivityResultContracts.GetContent()),
    UUFileProvider
{
    private var completionBlock: ((Context?, Uri?)->Unit)? = null

    /**
     * Requests a file from the user using the system file picker.
     *
     * This method launches the Android file picker activity with the specified MIME type filter.
     * The completion callback will be invoked when the user selects a file or cancels the picker.
     *
     * **Note:** [init] must be called before this method can be used. If called before
     * initialization, a warning will be logged and the file picker will not launch.
     *
     * @param filter MIME type filter to restrict file selection (e.g., "image" + "/" + "*" for all images,
     *               "application/pdf" for PDF files, "video" + "/" + "*" for all videos).
     *               Use "*" + "/" + "*" to allow all file types.
     * @param completion Callback invoked when the file picker returns a result.
     *                  - The first parameter is a [Context] (typically the application context) for file operations,
     *                    or `null` if the activity is not available.
     *                  - The second parameter is the [Uri] of the selected file, or `null` if the user cancelled.
     *
     * Example usage:
     * ```kotlin
     * // Request an image file
     * UUFiles.requestFile("image" + "/" + "*") { context, uri ->
     *     uri?.let {
     *         // Process the selected image
     *         // context can be used for ContentResolver operations
     *         loadImageFromUri(context, it)
     *     } ?: run {
     *         // User cancelled
     *         showMessage("File selection cancelled")
     *     }
     * }
     *
     * // Request a PDF file
     * UUFiles.requestFile("application/pdf") { context, uri ->
     *     uri?.let {
     *         openPdf(context, it)
     *     }
     * }
     * ```
     */
    override fun requestFile(filter: String, completion: (Context?, Uri?) -> Unit)
    {
        completionBlock = completion
        launch(filter)
    }

    /**
     * Handles the result from the file picker activity.
     *
     * This method is called automatically by the Activity Result API when the file picker
     * returns a result. It invokes the completion callback that was provided to [requestFile]
     * with the application context and the selected file URI (or null if cancelled), then clears
     * the stored completion block.
     *
     * @param result The [Uri] of the selected file, or `null` if the user cancelled
     */
    override fun handleLaunchResult(result: Uri?)
    {
        val block = completionBlock ?: return
        completionBlock = null
        block(activity?.applicationContext, result)
    }
}