package com.example.rokuon

import android.content.Context
import android.os.Environment
import java.io.File

object RecordFileManager {
        fun getRecordFile(context: Context, recordId: Long): File?
                = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.resolve(recordId.toString())
}
