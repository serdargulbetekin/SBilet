package com.example.sbilet.modules.journeys


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbilet.databinding.RowJourneyBinding
import com.example.sbilet.util.hourFormat

class JourneyAdapter(private val onClick: (JourneyItem) -> Unit) :
    RecyclerView.Adapter<JourneyViewHolder>() {

    private val itemList = mutableListOf<JourneyItem>()

    fun updateData(itemList: List<JourneyItem>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyViewHolder {
        return JourneyViewHolder(
            RowJourneyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: JourneyViewHolder, position: Int) {
        holder.bind(itemList[position], onClick)
    }
}

class JourneyViewHolder(private val binding: RowJourneyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: JourneyItem, onClick: (JourneyItem) -> Unit) {
        binding.apply {
            textViewDeparture.text = item.departure.hourFormat()
            textViewArrival.text = item.arrival.hourFormat()
            textViewFrom.text = item.from
            textViewTo.text = item.to
            textViewPrice.text = item.price + " TL"
            linearLayoutContainer.setOnClickListener {
                onClick.invoke(item)
            }

        }

    }
}