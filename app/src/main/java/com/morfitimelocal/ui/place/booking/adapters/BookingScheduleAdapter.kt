package com.morfitimelocal.ui.place.booking.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morfitimelocal.core.BaseViewHolder
import com.morfitimelocal.data.model.place.booking.BookingSchedule
import com.morfitimelocal.databinding.RecyclerRowBookingScheduleBinding

class BookingScheduleAdapter(private val context: Context, private val onRowClickListener: OnRowClickListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var list = listOf<BookingSchedule>()

    fun updateList(updates: List<BookingSchedule>) {
        list = updates
        notifyDataSetChanged()
    }

    interface OnRowClickListener {
        fun onItemClickSchedule(item: BookingSchedule, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val binding = RecyclerRowBookingScheduleBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = UsersBookingScheduleViewHolder(binding)
        binding.root.setOnClickListener {
            val position = holder.absoluteAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
            onRowClickListener.onItemClickSchedule(list[position], position)
        }
        return holder
    }

    override fun getItemCount() : Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
       when (holder) {
           is UsersBookingScheduleViewHolder -> holder.bind(list[position])
       }
    }

    private inner class UsersBookingScheduleViewHolder(val binding: RecyclerRowBookingScheduleBinding) : BaseViewHolder<BookingSchedule>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: BookingSchedule) {
            binding.textSchedule.text = item.schedule
            when (item.capacity) {
                "1" -> binding.textCapacity.text = "Queda 1 lugar"
                else -> binding.textCapacity.text = "Quedan ${item.capacity} lugares"
            }
        }
    }
}