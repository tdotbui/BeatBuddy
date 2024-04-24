package edu.temple.beatbuddy.utils

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText

object Helpers {
    fun showMessage(
        context: Context,
        message: String?
    ) = makeText(context, message, LENGTH_SHORT).show()

    fun getUidFromSharedPreferences(context: Context): String? =
        context
            .getSharedPreferences(USER_UID, Context.MODE_PRIVATE)
            .getString(USER_UID, "")

    fun saveUidToSharedPreferences(context: Context, uid: String) {
        context
            .getSharedPreferences(USER_UID, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_UID, uid)
            .apply()
    }
}

fun Long.toTime(): String {
    val stringBuffer = StringBuffer()

    val minutes = (this / 60000).toInt()
    val seconds = (this % 60000 / 1000).toInt()

    stringBuffer
        .append(String.format("%02d", minutes))
        .append(":")
        .append(String.format("%02d", seconds))

    return stringBuffer.toString()
}

const val USER_UID = "userUid"