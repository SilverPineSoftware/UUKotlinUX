package com.silverpine.uu.ux.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.silverpine.uu.ux.R
import com.silverpine.uu.ux.uuAddOrShowFragment
import com.silverpine.uu.ux.uuHideOverlayFragment
import com.silverpine.uu.ux.uuReplaceMainFragment

open class UUFragmentViewModel: UUViewModel()
{
    var uuReplaceMainFragment: ((Any, Bundle?)->Unit) = { _, _ -> }
    var uuShowOverlayFragment: ((Any, Bundle?)->Unit) = { _, _ -> }
    var uuHideOverlayFragment: ((Any)->Unit) = { }
}

interface UUFragmentProvider
{
    fun createFragment(tag: Any, args: Bundle?): Fragment?
}

fun FragmentActivity.uuSetupFragmentViewModel(viewModel: UUFragmentViewModel, fragmentProvider: UUFragmentProvider)
{
    uuSetupViewModel(viewModel)

    viewModel.uuReplaceMainFragment =
        { tag, bundle ->
            val fragment = fragmentProvider.createFragment(tag, bundle)
            fragment?.let()
            {
                uuReplaceMainFragment(it, "$tag")
            }
        }

    viewModel.uuShowOverlayFragment =
        { tag, bundle ->
            val fragment = fragmentProvider.createFragment(tag, bundle)
            fragment?.let()
            {
                uuAddOrShowFragment(it, R.id.uu_overlay_frame, "$tag")
            }
        }

    viewModel.uuHideOverlayFragment =
        { tag ->
            uuHideOverlayFragment("$tag")
        }
}