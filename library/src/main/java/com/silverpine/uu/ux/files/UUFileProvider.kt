package com.silverpine.uu.ux.files

import android.content.Context
import android.net.Uri

/**
 * Interface for requesting files from the user via the system file picker.
 *
 * This interface provides a clean abstraction for launching the Android file picker
 * and receiving the selected file URI. Implementations handle the activity result
 * flow and provide the selected file URI (or null if cancelled) via a completion callback.
 *
 * @see [UUFiles] for the default implementation
 *
 * Example usage:
 * ```kotlin
 * // Initialize in onCreate
 * override fun onCreate(savedInstanceState: Bundle?)
 * {
 *     super.onCreate(savedInstanceState)
 *     UUFiles.init(this)
 * }
 *
 * // Request a file
 * UUFiles.requestFile("image" + "/" + "*") { context, uri ->
 *     if (uri != null)
 *     {
 *         // User selected a file
 *         processFile(uri)
 *     }
 *     else
 *     {
 *         // User cancelled the file picker
 *         onFileSelectionCancelled()
 *     }
 * }
 * ```
 */
interface UUFileProvider
{
    /**
     * Requests a file from the user using the system file picker.
     *
     * This method launches the Android file picker activity with the specified MIME type filter.
     * The completion callback will be invoked when the user selects a file or cancels the picker.
     *
     * **Note:** The implementing class must be initialized (typically via [com.silverpine.uu.ux.UUActivityLauncher.init])
     * before this method can be used.
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
     * fileProvider.requestFile("image" + "/" + "*") { context, uri ->
     *     uri?.let {
     *         // Process the selected image
     *         loadImageFromUri(it)
     *     } ?: run {
     *         // User cancelled
     *         showMessage("File selection cancelled")
     *     }
     * }
     *
     * // Request a PDF file
     * fileProvider.requestFile("application/pdf") { context, uri ->
     *     uri?.let {
     *         openPdf(it)
     *     }
     * }
     * ```
     */
    fun requestFile(filter: String, completion: (Context?, Uri?)->Unit)
}