package com.silverpine.uu.sample.ux.binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityBindingBinding

class BindingActivity : AppCompatActivity()
{
    private lateinit var viewModel: BindingViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[BindingViewModel::class.java]
        val binding = ActivityBindingBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        viewModel.update()
    }
}