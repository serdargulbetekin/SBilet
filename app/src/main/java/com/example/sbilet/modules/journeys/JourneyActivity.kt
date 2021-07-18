package com.example.sbilet.modules.journeys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sbilet.databinding.ActivityJourneyBinding
import com.example.sbilet.modules.main.bus.LocationJourneyItem
import com.example.sbilet.util.getUIDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_journey.*

@AndroidEntryPoint
class JourneyActivity : AppCompatActivity() {

    private val viewBinding by lazy { ActivityJourneyBinding.inflate(layoutInflater) }

    private val viewModel: JourneyViewModel by viewModels()
    private val adapter by lazy {
        JourneyAdapter {
            onJourneyClick(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.apply {
            recyclerView.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(this@JourneyActivity)
            recyclerView.adapter = adapter

        }

        viewModel.locationJourneyItem.observe(this, {
            toolbar.show(
                title = it.from.name + " - " + it.to.name,
                subTitle = it.date.getUIDate(),
                showBack = { onBackPressed() }
            )
        })

        viewModel.progress.observe(this, {
            if (it) {
                viewBinding.progressBar.visibility = View.VISIBLE
            } else {
                viewBinding.progressBar.visibility = View.GONE
            }
        })

        viewModel.journeyList.observe(this, {
            if (it.isEmpty()) {
                Toast.makeText(
                    this,
                    "Seyehat bilgisi bulunamadı.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                adapter.updateData(it)
            }
        })

        viewModel.errorMessage.observe(this, {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({ finish() }, 1000)
        })

    }

    private fun onJourneyClick(journeyItem: JourneyItem) {
        Toast.makeText(
            this,
            journeyItem.from + " - " + journeyItem.to + "seçildi",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val EXTRAS_LOCATION_JOURNEY = "EXTRAS_LOCATION_JOURNEY"

        fun createIntent(
            context: Context,
            locationJourneyItem: LocationJourneyItem
        ) =
            Intent(context, JourneyActivity::class.java).apply {
                putExtra(EXTRAS_LOCATION_JOURNEY, locationJourneyItem)
            }
    }
}