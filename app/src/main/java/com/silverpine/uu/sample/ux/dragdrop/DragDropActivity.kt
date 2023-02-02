package com.silverpine.uu.sample.ux.dragdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityDragDropBinding

class DragDropActivity : AppCompatActivity()
{
    private lateinit var viewModel: DropManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DropManagerViewModel::class.java]
        val binding = ActivityDragDropBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        viewModel.update()
    }
}