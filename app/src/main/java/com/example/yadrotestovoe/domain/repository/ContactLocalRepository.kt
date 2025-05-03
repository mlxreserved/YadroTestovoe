package com.example.yadrotestovoe.domain.repository

import com.example.yadrotestovoe.domain.model.Contact

interface ContactLocalRepository {

    suspend fun getContacts() : List<Contact>

}