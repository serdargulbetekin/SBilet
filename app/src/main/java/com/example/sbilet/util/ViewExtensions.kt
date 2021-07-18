package com.example.sbilet.util

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.setTextColorRes(@ColorRes resId: Int) {
    setTextColor(
        ContextCompat.getColor(
            context,
            resId
        )
    )
}