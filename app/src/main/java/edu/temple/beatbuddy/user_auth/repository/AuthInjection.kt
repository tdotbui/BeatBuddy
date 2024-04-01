package edu.temple.beatbuddy.user_auth.repository

import com.google.firebase.firestore.FirebaseFirestore

object AuthInjection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore {
        return instance
    }
}