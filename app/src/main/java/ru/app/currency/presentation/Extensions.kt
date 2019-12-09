package ru.app.currency.presentation

import android.widget.EditText

fun EditText.updateText(text: String) {
    val focused = hasFocus()
    if (focused) {
        clearFocus()
    }
    setText(text)
    if (focused) {
        requestFocus()
    }
}