package com.sun.weatherapp.data.reposiroty

import com.sun.weatherapp.data.service.FirestoreService

class FirebaseRepository {
    private val db = FirestoreService.db

    /**
     * Thêm mới hoặc ghi đè tài liệu
     */
    fun setDocument(
        collectionPath: String,
        data: Any,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionPath)
            .document()
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

}
