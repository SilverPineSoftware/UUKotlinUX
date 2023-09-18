package com.silverpine.uu.sample.ux.animation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.slider.Slider
import com.silverpine.uu.sample.ux.databinding.ActivityFadeAnimationBinding

class FadeAnimationActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[FadeAnimationViewModel::class.java]
        val binding = ActivityFadeAnimationBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}

@InverseBindingAdapter(attribute = "android:value")
fun getSliderValue(slider: Slider) = slider.value

@BindingAdapter( "android:valueAttrChanged")
fun setSliderListeners(slider: Slider, attrChange: InverseBindingListener)
{
    slider.addOnChangeListener()
    { _, _, _ ->
        attrChange.onChange()
    }
}