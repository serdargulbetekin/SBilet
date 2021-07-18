package com.example.sbilet.modules.main.plane

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.sbilet.R
import com.example.sbilet.databinding.FragmentBusBinding
import com.example.sbilet.databinding.FragmentPlaneBinding
import com.example.sbilet.modules.main.bus.BusViewModel
import com.example.sbilet.uikit.locationselection.LocationSelectionDialog
import com.example.sbilet.util.showDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class PlaneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewBinding by lazy { FragmentPlaneBinding.inflate(layoutInflater) }
    private val viewModel: PlaneViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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


        viewModel.sBiletDepartureDate.observe(this, {
            viewBinding.textViewDepartureTicket.text = it.toString()
        })


        viewBinding.apply {
            linearLayoutFrom.setOnClickListener {
                viewModel.locationItem.observe(this@PlaneFragment, {
                    LocationSelectionDialog(
                        this@PlaneFragment.requireContext(),
                        "Nereden",
                        it,
                        onSelect = { busPlaneLocationItem ->
                            viewModel.onSelectFrom(busPlaneLocationItem)
                        }).show()
                })
            }
            linearLayoutTo.setOnClickListener {
                viewModel.locationItem.observe(this@PlaneFragment, {
                    LocationSelectionDialog(
                        this@PlaneFragment.requireContext(),
                        "Nereye",
                        it,
                        onSelect = { busPlaneLocationItem ->
                            viewModel.onSelectTo(busPlaneLocationItem)
                        }).show()
                })
            }

            textViewFindTicket.setOnClickListener {
                viewModel.onClickFindTicket()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}