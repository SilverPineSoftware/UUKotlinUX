package com.silverpine.uu.sample.ux.text

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.activities.BaseActivity
import com.silverpine.uu.sample.ux.databinding.ActivityTypewriterTextBinding

class TypewriterTextActivity : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[TypewriterTextViewModel::class.java]
        val binding = ActivityTypewriterTextBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setupViewModel(viewModel)
        viewModel.update()
    }
}