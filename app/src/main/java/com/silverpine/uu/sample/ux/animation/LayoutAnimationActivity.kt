package com.silverpine.uu.sample.ux.animation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.databinding.ActivityLayoutAnimationBinding
import com.silverpine.uu.ux.UUMenuHandler

class LayoutAnimationActivity : AppCompatActivity()
{
    private lateinit var viewModel: LayoutAnimationViewModel
    private lateinit var menuHandler: UUMenuHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LayoutAnimationViewModel::class.java]
        val binding = ActivityLayoutAnimationBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val m = menu ?: return false
        menuHandler = UUMenuHandler(m)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean
    {
        menu?.removeGroup(0)

        menuHandler.add("Hide All")
        {
            viewModel.hideAll()
        }

        menuHandler.add("Show 1")
        {
            viewModel.showOne()
        }

        menuHandler.add("Show 2")
        {
            viewModel.showTwo()
        }

        menuHandler.add("Show 3")
        {
            viewModel.showThree()
        }

        menuHandler.add("Show 4")
        {
            viewModel.showFour()
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }
}