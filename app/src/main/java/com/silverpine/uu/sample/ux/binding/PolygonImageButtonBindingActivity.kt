package com.silverpine.uu.sample.ux.binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityPolygonImageButtonBindingBinding

class PolygonImageButtonBindingActivity : AppCompatActivity()
{
    private lateinit var viewModel: PolygonImageButtonBindingViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[PolygonImageButtonBindingViewModel::class.java]
        val binding = ActivityPolygonImageButtonBindingBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        viewModel.update()
    }
}
