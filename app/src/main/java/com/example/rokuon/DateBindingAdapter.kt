package com.example.rokuon

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter(value = ["android:text"], requireAll = false)
fun setDateAsText(textView: TextView, date: Date) {
    val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN)
    textView.text = df.format(date)
}