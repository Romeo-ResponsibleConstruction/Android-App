package com.example.rc_app.storage

interface InternalRepository<T> {
    fun saveToInternalStorage(filetype: T): String
    fun getFromInternalStorage(filename: String)
    fun deleteFromInternalStorage(filename: String)
}