package com.example.rc_app.storage

interface Repository<T> {
    fun save(entity: T): String
    fun read(filepath: String): T
    fun delete(entity: T): Boolean
}