package com.silverpine.uu.ux.media

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.logging.logException

private const val LOG_TAG = "UUTextureViewVideoPlayer"

class UUTextureViewVideoPlayer(
    private val textureView: TextureView,
    @RawRes private val videoResourceId: Int,
    looping: Boolean = false):
        AutoCloseable,
        SurfaceTextureListener,
        MediaPlayer.OnPreparedListener
{
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(textureView.context, videoResourceId)
    private var surface: Surface? = null

    init
    {
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.isLooping = looping
    }

    fun stop()
    {
        mediaPlayer.stop()
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int)
    {
        this.surface?.release()
        val s = Surface(surface)
        this.surface = s
        mediaPlayer.setSurface(s)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int)
    {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean
    {
        this.surface?.release()
        this.surface = null
        mediaPlayer.setSurface(null)
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture)
    {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // MediaPlayer.OnPreparedListener
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onPrepared(mp: MediaPlayer?)
    {
        try
        {
            val videoRatio = mediaPlayer.videoWidth.toFloat() / mediaPlayer.videoHeight.toFloat()
            val screenRatio = textureView.width.toFloat() / textureView.height.toFloat()
            val scaleX = videoRatio / screenRatio

            if (scaleX.isFinite())
            {
                if (scaleX >= 1.0f)
                {
                    textureView.scaleX = scaleX
                }
                else
                {
                    textureView.scaleY = 1f / scaleX
                }
            }

            if (!mediaPlayer.isPlaying)
            {
                mediaPlayer.start()
            }
        }
        catch (ex: Exception)
        {
            UULog.logException(LOG_TAG, "onPrepared", ex)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // AutoCloseable
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun close()
    {
        try
        {
            UULog.debug(LOG_TAG, "close, ${this}, Shutting object down and releasing resources")

            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
            surface?.release()
        }
        catch (ex: Exception)
        {
            UULog.logException(LOG_TAG, "close", ex)
        }
    }
}


@BindingAdapter("uuLoopingVideo")
fun TextureView.uuBindLoopingVideo(@RawRes source: Int)
{
    uuBindVideo(source, true)
}

@BindingAdapter("uuVideo")
fun TextureView.uuBindVideo(@RawRes source: Int)
{
    uuBindVideo(source, false)
}

fun TextureView.uuBindVideo(@RawRes source: Int, looping: Boolean)
{
    if (source == 0)
    {
        (tag as? UUTextureViewVideoPlayer)?.stop()
        tag = null
        surfaceTextureListener = null
        return
    }

    surfaceTextureListener = UUTextureViewVideoPlayer(this, source, looping)
}
