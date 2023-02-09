package com.silverpine.uu.sample.ux

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityCircularImageViewBinding

class CircularImageButtonActivity : AppCompatActivity()
{
    private lateinit var viewModel: CircularImageButtonViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[CircularImageButtonViewModel::class.java]
        val binding = ActivityCircularImageViewBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        viewModel.update()
    }
}
