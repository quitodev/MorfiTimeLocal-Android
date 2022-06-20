package com.morfitimelocal.ui.place.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morfitimelocal.R
import com.morfitimelocal.application.State
import com.morfitimelocal.application.mapSuccess
import com.morfitimelocal.data.model.place.booking.BookingSchedule
import com.morfitimelocal.data.model.place.booking.BookingSector
import com.morfitimelocal.databinding.FragmentBookingBinding
import com.morfitimelocal.presentation.place.booking.BookingViewModel
import com.morfitimelocal.ui.place.booking.adapters.BookingDaysAdapter
import com.morfitimelocal.ui.place.booking.adapters.BookingScheduleAdapter
import com.morfitimelocal.ui.place.booking.adapters.BookingSectorAdapter
import com.morfitimelocal.ui.place.booking.dialogs.BookingDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class BookingFragment : Fragment(), BookingSectorAdapter.OnRowClickListener, BookingDaysAdapter.OnRowClickListener, BookingScheduleAdapter.OnRowClickListener {

    // VIEW BINDING
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    // VIEW MODEL
    private val viewModel by activityViewModels<BookingViewModel>()

    // ADAPTERS
    private lateinit var bookingSectorAdapter: BookingSectorAdapter
    private lateinit var bookingDaysAdapter: BookingDaysAdapter
    private lateinit var bookingScheduleAdapter: BookingScheduleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bookingSectorAdapter = BookingSectorAdapter(requireContext(), this)
        bookingDaysAdapter = BookingDaysAdapter(requireContext(), this)
        bookingScheduleAdapter = BookingScheduleAdapter(requireContext(), this)
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBookings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBookings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(STARTED) {
                viewModel.getBookings.collect { result ->
                    when (result) {
                        is State.Loading -> binding.layoutProgress.visibility = View.VISIBLE
                        is State.Success -> {
                            when {
                                result.data.sectors.isNotEmpty() -> {
                                    binding.layoutProgress.visibility = View.GONE
                                    when (binding.recyclerSectors.adapter) {
                                        null -> {
                                            binding.layoutSector.visibility = View.VISIBLE
                                            binding.layoutSector.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_right))
                                            binding.recyclerSectors.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                                            bookingSectorAdapter.updateList(result.data.sectors)
                                            binding.recyclerSectors.adapter = bookingSectorAdapter
                                        }
                                        else -> bookingSectorAdapter.updateList(result.data.sectors)
                                    }
                                }
                            }
                        }
                        is State.Failure -> {
                            binding.layoutProgress.visibility = View.GONE
                            when {
                                "NullPointerException" in result.exception.toString() -> { }
                                "Missing or insufficient permissions" in result.exception.toString() -> { }
                                "NullPointerException" in result.exception.toString() -> { }
                                "Unable to resolve host" in result.exception.toString() -> {
                                    Toast.makeText(context, "No se pudo conectar. Revisá tu conexión!", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(context, "Problemas de conexión! Si continúa, escribinos a soporte@morfitime.com.ar",
                                        Toast.LENGTH_LONG).show()
                                    Log.e("FIRESTORE DOCUMENTS", result.exception.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onItemClickSector(item: BookingSector, position: Int) {
        viewModel.getBookings.value.mapSuccess { result ->
            when {
                result.days.isNotEmpty() -> {
                    binding.layoutSchedule.visibility = View.GONE
                    binding.layoutDays.visibility = View.GONE
                    binding.layoutDays.visibility = View.VISIBLE
                    binding.layoutDays.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_right))
                    when (binding.recyclerDays.adapter) {
                        null -> {
                            binding.recyclerDays.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                            bookingDaysAdapter.updateList(result.days, item)
                            binding.recyclerDays.adapter = bookingDaysAdapter
                        }
                        else -> bookingDaysAdapter.updateList(result.days, item)
                    }
                    Timer().schedule(200){
                        binding.scrollView.smoothScrollTo(0,5000)
                    }
                }
                else -> { }
            }
        }
    }

    override fun onItemClickDay(item: String, position: Int, sector: BookingSector) {
        viewModel.getBookings.value.mapSuccess { result ->
            when {
                result.schedule.isNotEmpty() -> {
                    val list = mutableListOf<BookingSchedule>()
                    result.schedule.forEach { schedule ->
                        when {
                            schedule.sector == sector.sector && schedule.schedule.contains(item) -> list.add(schedule)
                        }
                    }
                    list.sortWith(compareBy<BookingSchedule> { it.date }.thenBy { it.hour })
                    when {
                        list.isNotEmpty() -> {
                            binding.layoutSchedule.visibility = View.GONE
                            binding.layoutSchedule.visibility = View.VISIBLE
                            binding.layoutSchedule.startAnimation(AnimationUtils.loadAnimation(context, R.anim.from_right))
                            when (binding.recyclerSchedule.adapter) {
                                null -> {
                                    binding.recyclerSchedule.layoutManager = LinearLayoutManager(requireContext())
                                    bookingScheduleAdapter.updateList(list)
                                    binding.recyclerSchedule.adapter = bookingScheduleAdapter
                                }
                                else -> bookingScheduleAdapter.updateList(list)
                            }
                            Timer().schedule(200){
                                binding.scrollView.smoothScrollTo(0,5000)
                            }
                        }
                        else -> {
                            binding.layoutSchedule.visibility = View.GONE
                            Toast.makeText(context, "No hay reservas disponibles para el día elegido!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> { }
            }
        }
    }

    override fun onItemClickSchedule(item: BookingSchedule, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("item", item)

        val bookingDialogFragment = BookingDialogFragment()
        bookingDialogFragment.arguments = bundle

        findNavController().navigate(R.id.bookingDialogFragment, bundle)
    }
}