package com.example.sbilet.util

import android.app.Activity
import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import java.util.*


fun Activity.showDatePickerDialog(
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    listener: (SbiletDate) -> Unit
) {

    val instance = Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("GMT+3")
    }

    val datePickerDialog = DatePickerDialog(
        this,
        0,
        { _, year, month, day ->
            val sbiletDate = SbiletDate(day, month + 1, year, "00", "00", "00")
            listener.invoke(sbiletDate)
        },
        instance.get(Calendar.YEAR),
        instance.get(Calendar.MONTH),
        instance.get(Calendar.DAY_OF_MONTH)
    )


    if (minDate != null) {
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
    }
    if (maxDate != null) {
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
    }
    datePickerDialog.show()
}

fun Fragment.showDatePickerDialog(
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    listener: (SbiletDate) -> Unit
) {
    this.activity?.showDatePickerDialog(minDate, maxDate, listener)
}


