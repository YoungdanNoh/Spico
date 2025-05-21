package com.a401.spicoandroid.common.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object FileUtil {
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Video.Media.DATA), null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return uri.path // fallback
    }
}
