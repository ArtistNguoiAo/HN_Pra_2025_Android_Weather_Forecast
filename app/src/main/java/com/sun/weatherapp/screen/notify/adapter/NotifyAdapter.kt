package com.sun.weatherapp.screen.notify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.weatherapp.databinding.ItemNotifyBinding

class NotifyAdapter(
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<String, NotifyAdapter.NotifyViewHolder>(NotifyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val binding = ItemNotifyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotifyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotifyViewHolder(
        val binding: ItemNotifyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notify: String) {
            binding.apply {
                tvTime.text = notify
                btnDelete.setOnClickListener {
                    onDeleteClick(notify)
                }
            }
        }
    }

    private class NotifyDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
