package com.example.sbilet.uikit.locationselection

import android.content.Context
import android.os.Bundle
import com.example.sbilet.databinding.DialogLocationSelectionBinding
import com.example.sbilet.modules.main.bus.BusPlaneLocationItem

class LocationSelectionDialog(
    context: Context,
    private val toolbarTitle: String,
    private val items: List<BusPlaneLocationItem>,
    private val onSelect: (BusPlaneLocationItem) -> Unit
) : BaseFullScreenDialog(context) {

    private val viewBinding by lazy { DialogLocationSelectionBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        LocationSelectionAdapter {
            onSelect(it)
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.apply {
            toolbar.show(toolbarTitle, showBack = { dismiss() })
            recyclerView.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(context)
            recyclerView.adapter = adapter
            adapter.updateData(items)
        }

    }
}

