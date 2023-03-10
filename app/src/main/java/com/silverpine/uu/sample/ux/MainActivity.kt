package com.silverpine.uu.sample.ux

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.binding.BindingActivity
import com.silverpine.uu.sample.ux.databinding.ActivityMainBinding
import com.silverpine.uu.sample.ux.dragdrop.DragDropActivity
import com.silverpine.uu.ux.UUMenuHandler

class MainActivity : AppCompatActivity()
{
    private lateinit var viewModel: MainViewModel
    private lateinit var menuHandler: UUMenuHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        viewModel.update()
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

        menuHandler.add(R.string.activity_drag_drop_title)
        {
            startActivity(Intent(this, DragDropActivity::class.java))
        }

        menuHandler.add(R.string.activity_binding_title)
        {
            startActivity(Intent(this, BindingActivity::class.java))
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }
}
