package com.example.yadrotestovoe.domain.repository

interface AidlRepository {

    suspend fun bindService()
    suspend fun deleteDuplicateContacts(): Result<Int>
    fun unbindService()

}