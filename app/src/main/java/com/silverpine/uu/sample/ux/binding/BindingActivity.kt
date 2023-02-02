package com.silverpine.uu.sample.ux.binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.core.uuGetOrNull
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.sample.ux.databinding.ActivityBindingBinding
import com.silverpine.uu.ux.UUBorderedImageView

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

        val borderViews: ArrayList<UUBorderedImageView> = arrayListOf(
            binding.bordered0,
            binding.bordered1,
            binding.bordered2,
            binding.bordered3,
            binding.bordered4,
            binding.bordered5,
        )

        viewModel.onTapBorderView =
        { index ->
            borderViews.uuGetOrNull(index)?.apply()
            {
                if (borderColor == UUResources.getColor(R.color.red))
                {
                    borderColor = UUResources.getColor(R.color.yellow)
                    borderWidth = 40.0f
                }
                else
                {
                    borderColor = UUResources.getColor(R.color.red)
                    borderWidth = 20.0f
                }
            }
        }
    }
}