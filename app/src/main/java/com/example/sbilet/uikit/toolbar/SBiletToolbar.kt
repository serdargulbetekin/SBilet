package com.example.sbilet.uikit.toolbar

import com.example.sbilet.databinding.LayoutSbiletToolbarBinding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes

class SBiletToolbar @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleArr: Int = 0
) : FrameLayout(context, attributes, defStyleArr) {
    private val binding by lazy {
        LayoutSbiletToolbarBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun show(
        title: CharSequence,
        subTitle: CharSequence?=null,
        showBack: (() -> Unit)? = null,
        showMenu: (() -> Unit)? = null,
    ) {
        binding.apply {
            textViewTitle.text = title
            if (subTitle!=null){
                textSubTitle.text = subTitle
                textSubTitle.visibility = View.VISIBLE
            }
            if (showBack != null) {
                imageViewBack.visibility = View.VISIBLE
                imageViewBack.setOnClickListener { showBack() }
            }
            if (showMenu != null) {
                imageViewMenu.visibility = View.VISIBLE
                imageViewMenu.setOnClickListener { showMenu() }
            }
        }
    }

}