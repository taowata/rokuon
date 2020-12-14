package com.example.rokuon

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter(value = ["android:text"], requireAll = false)
fun setLocalDateTimeAsText(textView: TextView, localDateTime: LocalDateTime) {
    val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    textView.text = dtf.format(localDateTime)
}
