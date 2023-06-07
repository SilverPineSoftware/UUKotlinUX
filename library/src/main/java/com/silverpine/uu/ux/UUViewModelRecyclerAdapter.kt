package com.silverpine.uu.ux

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.silverpine.uu.core.UUThread

class UUViewModelRecyclerAdapter(private val lifecycleOwner: LifecycleOwner, private val rowTapped: ((ViewModel)->Unit)): RecyclerView.Adapter<UUViewModelRecyclerAdapter.ViewHolder>()
{
    private val data: ArrayList<ViewModel> = ArrayList()
    private var viewTypes: ArrayList<Class<out ViewModel>> = ArrayList()
    private var layoutTypes: ArrayList<Int> = ArrayList()
    private var bindingIds: ArrayList<Int> = ArrayList()

    fun registerClass(viewModelClass: Class<out ViewModel>, @LayoutRes layoutResourceId: Int, bindingId: Int)
    {
        viewTypes.add(viewModelClass)
        layoutTypes.add(layoutResourceId)
        bindingIds.add(bindingId)
    }

    fun update(list: List<ViewModel>)
    {
        synchronized(data)
        {
            data.clear()
            data.addAll(list)
        }

        UUThread.runOnMainThread()
        {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UUViewModelRecyclerAdapter.ViewHolder
    {
        val layoutId = layoutTypes[viewType]
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layoutId, parent, false)
        return ViewHolder(lifecycleOwner, view, bindingIds[viewType], rowTapped)
    }

    override fun getItemViewType(position: Int): Int
    {
        val item = getItem(position)
        if (item == null)
        {
            throw RuntimeException("Unable to get model at position $position")
        }

        val viewType = viewTypes.indexOf(item::class.java)
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

    private fun getItem(position: Int): ViewModel?
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

    inner class ViewHolder(private val lifecycleOwner: LifecycleOwner, view: View, private val bindingId: Int, private val rowTapped: ((ViewModel)->Unit)) : RecyclerView.ViewHolder(view)
    {
        private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

        fun bind(model: ViewModel)
        {
            binding?.lifecycleOwner = lifecycleOwner
            binding?.setVariable(bindingId, model)
            binding?.executePendingBindings()

            itemView.setOnClickListener()
            {
                rowTapped.invoke(model)
            }
        }

        fun recycle()
        {
            binding?.unbind()
        }
    }
}