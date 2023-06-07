package com.silverpine.uu.ux

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class UURecyclerActivity: UUAppCompatActivity(layoutResourceId = R.layout.uu_recycler_activity)
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