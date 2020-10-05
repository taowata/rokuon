package com.example.rokuon

import android.content.Context
import android.os.Environment

object Constants {
        val dirPath: (Context) -> String = {
            it.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath.toString()
        }
}
