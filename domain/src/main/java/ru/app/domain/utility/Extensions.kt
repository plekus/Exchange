package ru.app.domain.utility

import java.util.*

fun Float.format2decimals(): String {
    return String.format(Locale.US, "%.2f", this)
}