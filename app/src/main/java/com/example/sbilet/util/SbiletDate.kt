package com.example.sbilet.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class SbiletDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: String,
    val minute: String,
    val second: String
) : Parcelable {
    override fun toString(): String {
        return "$day.$month.$year"
    }


    fun isEqual(sbiletDate: SbiletDate): Boolean {
        return sbiletDate.day == day && sbiletDate.month == month && sbiletDate.year == year
    }


    companion object {


        fun today(): SbiletDate {
            val instance = Calendar.getInstance().apply {
                timeZone = TimeZone.getTimeZone("GMT+3")
            }
            return SbiletDate(
                instance.get(Calendar.DAY_OF_MONTH),
                instance.get(Calendar.MONTH) + 1,
                instance.get(Calendar.YEAR),
                instance.get(Calendar.HOUR).toString(),
                instance.get(Calendar.MINUTE).toString(),
                instance.get(Calendar.SECOND).toString()
            )
        }

        fun tomorrow(): SbiletDate {
            val instance = Calendar.getInstance().apply {
                timeZone = TimeZone.getTimeZone("GMT+3")
            }
            instance.add(Calendar.DAY_OF_MONTH, 1)
            return SbiletDate(
                instance.get(Calendar.DAY_OF_MONTH),
                instance.get(Calendar.MONTH) + 1,
                instance.get(Calendar.YEAR),
                instance.get(Calendar.HOUR).toString(),
                instance.get(Calendar.MINUTE).toString(),
                instance.get(Calendar.SECOND).toString()
            )
        }

        fun convertToSbiletDate(input: String): SbiletDate {
            val dateAndHourList = input.split("T")
            val date = dateAndHourList[0].split("-")
            val hour = dateAndHourList[1].split(":")
            return if (date.size == 3) {
                if (hour.size == 3) {
                    SbiletDate(
                        date[0].toInt(),
                        date[1].toInt(),
                        date[2].toInt(),
                        hour[0],
                        hour[1],
                        hour[2],
                    )
                } else {
                    SbiletDate(
                        date[0].toInt(),
                        date[1].toInt(),
                        date[2].toInt(),
                        "00",
                        "00",
                        "00"
                    )
                }

            } else {
                SbiletDate(0, 0, 0, "00", "00", "00")
            }
        }
    }
}