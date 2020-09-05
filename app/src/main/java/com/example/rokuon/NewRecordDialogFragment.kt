package com.example.rokuon

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class NewRecordDialogFragment : DialogFragment() {

    private var number: String? = "number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            number = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val editText = EditText(context)

            editText.apply {
                hint = "新しい録音の名前を入力してください"
                val text = "録音$number"
                setText(text)
            }

            builder.setView(editText)
                .setTitle("新しい録音に名前をつけてください")
                .setPositiveButton("保存") { _, _ ->

                }
                .setNegativeButton("キャンセル") { _, _ ->

                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {

        private const val ARG_PARAM1 = "number"

        fun newInstance(order: Int) = NewRecordDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, order.toString())
            }
        }
    }
}