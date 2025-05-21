package com.a401.spicoandroid.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun openExternalLink(context: Context, url: String) {
    if (url.isBlank()) {
        Toast.makeText(context, "유효하지 않은 링크입니다.", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "링크를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
    }
}