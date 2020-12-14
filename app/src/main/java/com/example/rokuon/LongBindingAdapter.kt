package com.example.rokuon

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["android:text"], requireAll = false)
fun setLongAsText(textView: TextView, recordTime: Long) {
    textView.text = recordTime.toString()
}