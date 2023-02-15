package com.example.rc_app.infrastructure

interface Repository<T> {
    fun create(filetype: T): String
    fun read(filepath: String): T
    fun delete(filepath: String): Boolean
}