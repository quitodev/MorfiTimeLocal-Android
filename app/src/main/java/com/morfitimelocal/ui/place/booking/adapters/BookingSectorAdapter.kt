package com.morfitimelocal.ui.place.booking.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morfitimelocal.R
import com.morfitimelocal.core.BaseViewHolder
import com.morfitimelocal.data.model.place.booking.BookingSector
import com.morfitimelocal.databinding.RecyclerRowBookingSectorBinding

class BookingSectorAdapter(private val context: Context, private val onRowClickListener: OnRowClickListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var list = listOf<BookingSector>()
    private var selectedPosition = -1

    fun updateList(updates: List<BookingSector>) {
        list = updates
        notifyDataSetChanged()
    }

    interface OnRowClickListener {
        fun onItemClickSector(item: BookingSector, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val binding = RecyclerRowBookingSectorBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = UsersBookingSectorViewHolder(binding)
        binding.circleImage.setOnClickListener {
            val position = holder.absoluteAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
            selectedPosition = holder.absoluteAdapterPosition
            notifyDataSetChanged()
            onRowClickListener.onItemClickSector(list[position], position)
        }
        return holder
    }

    override fun getItemCount() : Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
       when (holder) {
           is UsersBookingSectorViewHolder -> holder.bind(list[position])
       }
    }

    private inner class UsersBookingSectorViewHolder(val binding: RecyclerRowBookingSectorBinding) : BaseViewHolder<BookingSector>(binding.root) {
        override fun bind(item: BookingSector) {
            binding.textName.text = item.name
            when (item.image) {
                "Vacío" -> binding.circleImage.setImageResource(R.drawable.ic_logo)
                else -> Glide.with(context).load(item.image).into(binding.circleImage)
            }
            when (absoluteAdapterPosition) {
                selectedPosition -> {
                    binding.circleImage.borderColor = Color.parseColor("#FF7B51")
                    binding.textName.setTextColor(Color.parseColor("#FF7B51"))
                }
                else -> {
                    binding.circleImage.borderColor = Color.parseColor("#A5A5A5")
                    binding.textName.setTextColor(Color.parseColor("#A5A5A5"))
                }
            }
        }
    }
}