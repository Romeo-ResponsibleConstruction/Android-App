package com.example.rc_app.data.datasource

interface DataSource<T> {
    fun save(entity: T): String
    fun read(filepath: String): T
    fun delete(entity: T): Boolean
}