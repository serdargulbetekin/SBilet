package com.example.sbilet.util


fun SbiletDate.serviceFormat() = "$year-$month-$day"
fun SbiletDate.hourFormat() = "$hour:$minute"
fun SbiletDate.isEqual(sbiletDate: SbiletDate): Boolean {
    return sbiletDate.day == day && sbiletDate.month == month && sbiletDate.year == year
}

fun SbiletDate.isGreaterThan(sbiletDate: SbiletDate) =
   year >  sbiletDate.year || month >  sbiletDate.month || day >  sbiletDate.day

fun SbiletDate.isLessThan(sbiletDate: SbiletDate) = !isGreaterThan(sbiletDate)

fun SbiletDate.getUIDate(): String {
    val month = when (this.month) {
        1 -> {
            "Ocak"
        }
        2 -> {
            "Şubat"
        }
        3 -> {
            "Mart"
        }
        4 -> {
            "Nisan"
        }
        5 -> {
            "Mayıs"
        }
        6 -> {
            "Haziran"
        }
        7 -> {
            "Temmuz"
        }
        8 -> {
            "Ağustos"
        }
        9 -> {
            "Eylül"
        }
        10 -> {
            "Ekim"
        }
        11 -> {
            "Kasım"
        }
        12 -> {
            "Aralık"
        }
        else -> ""
    }
    return this.day.toString() + " " + month + " " + this.year.toString()
}