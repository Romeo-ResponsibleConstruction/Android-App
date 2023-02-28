package com.example.rc_app.storage

interface InternalRepository<T> {
    fun saveToInternalStorage(filetype: T): String
    fun getFromInternalStorage(filepath: String): T
    fun deleteFromInternalStorage(filepath: String): Boolean
}