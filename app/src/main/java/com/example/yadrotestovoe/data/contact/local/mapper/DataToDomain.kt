package com.example.yadrotestovoe.data.contact.local.mapper

import com.example.yadrotestovoe.data.contact.local.model.ContactLocal
import com.example.yadrotestovoe.domain.model.Contact

fun mapContactLocalToContact(contactLocal: ContactLocal): Contact =
    Contact(
        id = contactLocal.id,
        name = contactLocal.name,
        phoneNumber = contactLocal.phoneNumber,
        thumbnailUri = contactLocal.thumbnailUri,
        phoneType = contactLocal.phoneType
    )