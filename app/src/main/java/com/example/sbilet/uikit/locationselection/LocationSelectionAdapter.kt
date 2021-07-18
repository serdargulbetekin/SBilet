package com.example.sbilet.uikit.locationselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbilet.databinding.RowLocationSelectionBinding
import com.example.sbilet.modules.main.bus.BusPlaneLocationItem

class LocationSelectionAdapter(private val onClick: (BusPlaneLocationItem) -> Unit) :
    RecyclerView.Adapter<LocationSelectionViewHolder>() {

    private val itemList = mutableListOf<BusPlaneLocationItem>()

    fun updateData(itemList: List<BusPlaneLocationItem>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationSelectionViewHolder {
        return LocationSelectionViewHolder(
            RowLocationSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: LocationSelectionViewHolder, position: Int) {
        holder.bind(itemList[position], onClick)
    }
}

class LocationSelectionViewHolder(private val binding: RowLocationSelectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BusPlaneLocationItem, onClick: (BusPlaneLocationItem) -> Unit) {
        binding.textView.apply {
            text =item.name
            setOnClickListener {
                onClick.invoke(item)
            }
        }

    }
}