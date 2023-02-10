package com.example.rc_app.storage

interface InternalRepository<T> {
    fun add(filetype: T)
    fun get(filename: String)
    fun delete(filename: String)
}