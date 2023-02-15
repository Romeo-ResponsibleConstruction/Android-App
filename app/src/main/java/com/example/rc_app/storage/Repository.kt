package com.example.rc_app.storage

interface Repository<T> {
    fun save(filetype: T): String
    fun read(filepath: String): T
    fun delete(filepath: String): Boolean
}