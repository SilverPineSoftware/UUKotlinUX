package com.silverpine.uu.sample.ux.binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityTextStyleBinding

class TextStyleActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[TextStyleViewModel::class.java]
        val binding = ActivityTextStyleBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        viewModel.update()
    }
}