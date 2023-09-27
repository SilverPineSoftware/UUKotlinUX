package com.silverpine.uu.sample.ux

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.sample.ux.animation.FadeAnimationActivity
import com.silverpine.uu.sample.ux.animation.LayoutAnimationActivity
import com.silverpine.uu.sample.ux.binding.BindingActivity
import com.silverpine.uu.sample.ux.binding.PolygonImageButtonBindingActivity
import com.silverpine.uu.sample.ux.databinding.ActivityMainBinding
import com.silverpine.uu.sample.ux.dialog.AlertDialogActivity
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

        menuHandler.add(R.string.activity_circular_image_button)
        {
            startActivity(Intent(this, CircularImageButtonActivity::class.java))
        }

        menuHandler.add(R.string.activity_polygon_image_button)
        {
            startActivity(Intent(this, PolygonImageButtonActivity::class.java))
        }

        menuHandler.add(R.string.activity_polygon_image_button_binding)
        {
            startActivity(Intent(this, PolygonImageButtonBindingActivity::class.java))
        }

        menuHandler.add(R.string.activity_layout_transition_animation)
        {
            startActivity(Intent(this, LayoutAnimationActivity::class.java))
        }

        menuHandler.add(R.string.activity_fade_animation)
        {
            startActivity(Intent(this, FadeAnimationActivity::class.java))
        }

        menuHandler.add(R.string.activity_alert_dialog)
        {
            startActivity(Intent(this, AlertDialogActivity::class.java))
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }
}
