package edu.temple.beatbuddy.repository.auth

import com.google.firebase.firestore.FirebaseFirestore

object AuthInjection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore {
        return instance
    }
}