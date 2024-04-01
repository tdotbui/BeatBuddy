package edu.temple.beatbuddy.utils

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText

object Helpers {
    fun showMessage(
        context: Context,
        message: String?
    ) = makeText(context, message, LENGTH_SHORT).show()
}