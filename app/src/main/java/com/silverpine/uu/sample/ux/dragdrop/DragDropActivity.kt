package com.silverpine.uu.sample.ux.dragdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityDragDropBinding
import com.silverpine.uu.ux.UUBorderedImageView
import com.silverpine.uu.ux.uuFadeAlpha

class DragDropActivity : AppCompatActivity()
{
    private lateinit var viewModel: DropManagerViewModel
    private val buttons: ArrayList<UUBorderedImageView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DropManagerViewModel::class.java]
        val binding = ActivityDragDropBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        buttons.add(binding.one)
        buttons.add(binding.two)
        buttons.add(binding.three)
        buttons.add(binding.four)

        viewModel.onFade = this::handleFade
        viewModel.update()
    }

    private fun handleFade(viewModel: DropViewModel, alpha: Float, duration: Long)
    {
        buttons.firstOrNull { (it.tag as? String) == viewModel.id }?.uuFadeAlpha(alpha, duration)

    }
}