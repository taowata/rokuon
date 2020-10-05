package com.example.rokuon

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter(value = ["android:text"], requireAll = false)
fun setLocalDateTimeAsText(textView: TextView, localDateTime: LocalDateTime) {
    val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    textView.text = dtf.format(localDateTime)
}