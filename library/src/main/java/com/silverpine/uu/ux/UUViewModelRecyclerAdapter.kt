package com.silverpine.uu.ux

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.silverpine.uu.core.uuDispatchMain

class UUViewModelRecyclerAdapter(private val lifecycleOwner: LifecycleOwner, private val rowTapped: ((UUAdapterItemViewModel)->Unit)? = null): RecyclerView.Adapter<UUViewModelRecyclerAdapter.ViewHolder>()
{
    private val data: ArrayList<UUAdapterItemViewModel> = ArrayList()
    private val viewModelTypes = ArrayList<UUAdapterItemViewModelMapping>()

    fun registerViewModel(mapping: UUAdapterItemViewModelMapping)
    {
        viewModelTypes.add(mapping)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<UUAdapterItemViewModel>)
    {
        synchronized(data)
        {
            data.clear()
            data.addAll(list)

            data.forEachIndexed()
            { index, item ->
                item.onDataChanged =
                {
                    uuDispatchMain()
                    {
                        notifyItemChanged(index)
                    }
                }
            }
        }

        uuDispatchMain()
        {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UUViewModelRecyclerAdapter.ViewHolder
    {
        val mapping = viewModelTypes[viewType]
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(mapping.layoutResourceId, parent, false)
        return ViewHolder(lifecycleOwner, view, mapping.bindingId, rowTapped)
    }

    override fun getItemViewType(position: Int): Int
    {
        val item = getItem(position)
            ?: throw RuntimeException("Unable to get model at position $position")

        val viewType = viewModelTypes.indexOfFirst { it.viewModelClass == item::class.java }
        if (viewType == -1)
        {
            throw RuntimeException("Unable to get view type for position $position, model: ${item::class.java}")
        }

        return viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = getItem(position)
        item?.let()
        {
            holder.bind(it)
        }
    }

    override fun onViewRecycled(holder: ViewHolder)
    {
        holder.recycle()
    }

    override fun getItemCount(): Int
    {
        synchronized(data)
        {
            return data.size
        }
    }

    private fun getItem(position: Int): UUAdapterItemViewModel?
    {
        synchronized(data)
        {
            if (position >= 0 && position < data.size)
            {
                return data[position]
            }
        }

        return null
    }

    inner class ViewHolder(private val lifecycleOwner: LifecycleOwner, view: View, private val bindingId: Int, private val rowTapped: ((UUAdapterItemViewModel)->Unit)? = null)  : RecyclerView.ViewHolder(view)
    {
        private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

        fun bind(model: UUAdapterItemViewModel)
        {
            binding?.lifecycleOwner = lifecycleOwner
            binding?.setVariable(bindingId, model)
            binding?.executePendingBindings()

            rowTapped?.let()
            { onRowTap ->
                itemView.setOnClickListener()
                {
                    onRowTap(model)
                }
            }
        }

        fun recycle()
        {
            binding?.unbind()
        }
    }
}