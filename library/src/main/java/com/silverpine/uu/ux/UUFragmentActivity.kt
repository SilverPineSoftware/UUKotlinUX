package com.silverpine.uu.ux

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity

open class UUFragmentActivity(@LayoutRes val layoutResourceId: Int = R.layout.uu_fragment_activity): FragmentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (layoutResourceId != -1)
        {
            setContentView(layoutResourceId)
        }
    }
}
