package com.silverpine.uu.ux

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class UURecyclerActivity(
    @StringRes titleResourceId: Int = -1,
    @LayoutRes layoutResourceId: Int = R.layout.uu_recycler_activity): UUAppCompatActivity(
    titleResourceId = titleResourceId,
    layoutResourceId = layoutResourceId)
{
    protected lateinit var adapter: UUViewModelRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UUViewModelRecyclerAdapter(this, this::handleRowTapped)
        recyclerView.adapter = adapter
        setupAdapter(recyclerView)
    }

    abstract fun setupAdapter(recyclerView: RecyclerView)

    open fun handleRowTapped(viewModel: ViewModel)
    {

    }
}