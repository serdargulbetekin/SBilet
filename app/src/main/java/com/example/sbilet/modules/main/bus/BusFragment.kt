package com.example.sbilet.modules.main.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sbilet.R
import com.example.sbilet.databinding.FragmentBusBinding
import com.example.sbilet.modules.journeys.JourneyActivity
import com.example.sbilet.uikit.locationselection.LocationSelectionDialog
import com.example.sbilet.util.setTextColorRes
import com.example.sbilet.util.showDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class BusFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewBinding by lazy { FragmentBusBinding.inflate(layoutInflater) }
    private val viewModel: BusViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewBinding.apply {
            textViewToday.setOnClickListener {
                arrangeColor(DateType.TODAY)
                viewModel.onFastDateClick(DateType.TODAY)
            }
            textViewTomorrow.setOnClickListener {
                arrangeColor(DateType.TOMORROW)
                viewModel.onFastDateClick(DateType.TOMORROW)
            }
        }

        viewModel.sBiletDepartureDate.observe(this, {
            viewBinding.textViewDate.text = it.first.toString()
            arrangeColor(it.second)
        })

        viewModel.progress.observe(this, {
            if (it) {
                viewBinding.progressBar.visibility = View.VISIBLE
            } else {
                viewBinding.progressBar.visibility = View.GONE
            }
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(
                this.requireContext(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        })

        viewModel.fromLocation.observe(this, {
            viewBinding.textViewFrom.text = it?.name
        })
        viewModel.toLocation.observe(this, {
            viewBinding.textViewTo.text = it?.name
        })

        viewModel.shouldContinueTriple.observe(
            this,
            { (shouldContinue, errorMessage, journeyItem) ->
                if (shouldContinue) {
                    startActivity(
                        JourneyActivity.createIntent(
                            this.requireContext(),
                            journeyItem!!
                        )
                    )
                } else {
                    Toast.makeText(this.requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            })

        viewBinding.apply {
            linearLayoutFrom.setOnClickListener {
                viewModel.locationItem.observe(this@BusFragment, {
                    LocationSelectionDialog(
                        this@BusFragment.requireContext(),
                        "Nereden",
                        it,
                        onSelect = { busPlaneLocationItem ->
                            viewModel.onSelectFrom(busPlaneLocationItem)
                        }).show()
                })
            }
            linearLayoutTo.setOnClickListener {
                viewModel.locationItem.observe(this@BusFragment, {
                    LocationSelectionDialog(
                        this@BusFragment.requireContext(),
                        "Nereye",
                        it,
                        onSelect = { busPlaneLocationItem ->
                            viewModel.onSelectTo(busPlaneLocationItem)
                        }).show()
                })
            }

            linearLayoutDate.setOnClickListener {
                showDatePickerDialog(minDate = Calendar.getInstance()) {
                    viewModel.setDate(it.first, it.second)
                    textViewDate.text = it.toString()
                }
            }

            textViewFindTicket.setOnClickListener {
                viewModel.onClickFindTicket()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = viewBinding.root

    private fun arrangeColor(dateType: DateType) {
        when (dateType) {
            DateType.TODAY -> {
                viewBinding.textViewToday.setBackgroundResource(R.drawable.shape_rectangle_dark_grey_radius_5)
                viewBinding.textViewToday.setTextColorRes(R.color.white)
                viewBinding.textViewTomorrow.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5_stroke)
                viewBinding.textViewTomorrow.setTextColorRes(R.color.dark_grey)
            }
            DateType.TOMORROW -> {
                viewBinding.textViewTomorrow.setBackgroundResource(R.drawable.shape_rectangle_dark_grey_radius_5)
                viewBinding.textViewTomorrow.setTextColorRes(R.color.white)
                viewBinding.textViewToday.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5_stroke)
                viewBinding.textViewToday.setTextColorRes(R.color.dark_grey)
            }
            DateType.NONE -> {
                viewBinding.textViewTomorrow.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5_stroke)
                viewBinding.textViewTomorrow.setTextColorRes(R.color.dark_grey)
                viewBinding.textViewToday.setBackgroundResource(R.drawable.shape_rectangle_grey_radius_5_stroke)
                viewBinding.textViewToday.setTextColorRes(R.color.dark_grey)
            }
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BusFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

enum class DateType {
    TODAY, TOMORROW, NONE
}