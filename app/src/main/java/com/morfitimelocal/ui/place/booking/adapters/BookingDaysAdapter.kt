package com.morfitimelocal.ui.place.booking.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morfitimelocal.core.BaseViewHolder
import com.morfitimelocal.data.model.place.booking.BookingSector
import com.morfitimelocal.databinding.RecyclerRowBookingDaysBinding

class BookingDaysAdapter(private val context: Context, private val onRowClickListener: OnRowClickListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var list = listOf<String>()
    private var sector = BookingSector()
    private var selectedPosition = -1

    fun updateList(updates: List<String>, bookingSector: BookingSector) {
        list = updates
        sector = bookingSector
        selectedPosition = -1
        notifyDataSetChanged()
    }

    interface OnRowClickListener {
        fun onItemClickDay(item: String, position: Int, sector: BookingSector)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val binding = RecyclerRowBookingDaysBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = UsersBookingDaysViewHolder(binding)
        binding.layoutDay.setOnClickListener {
            val position = holder.absoluteAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
            selectedPosition = holder.absoluteAdapterPosition
            notifyDataSetChanged()
            onRowClickListener.onItemClickDay(list[position], position, sector)
        }
        return holder
    }

    override fun getItemCount() : Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
       when (holder) {
           is UsersBookingDaysViewHolder -> holder.bind(list[position])
       }
    }

    private inner class UsersBookingDaysViewHolder(val binding: RecyclerRowBookingDaysBinding) : BaseViewHolder<String>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: String) {
            binding.textDay.text = "${item[0]}${item[1]}${item[2]}"
            binding.textNumber.text = "${item[4]}${item[5]}"
            binding.textMonth.text = "${item[7]}${item[8]}${item[9]}"
            when (absoluteAdapterPosition) {
                selectedPosition -> binding.cardDay.setCardBackgroundColor(Color.parseColor("#555555"))
                else -> binding.cardDay.setCardBackgroundColor(Color.parseColor("#2D2D2D"))
            }
        }
    }
}