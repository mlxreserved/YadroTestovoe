package com.example.yadrotestovoe.data.contact.local.util

import android.provider.ContactsContract.CommonDataKinds

object Projections {

    // Список аргументов, которые необходимо получить из таблицы
    val Number = arrayOf(
        CommonDataKinds.Phone.NUMBER,
        CommonDataKinds.Phone.IS_PRIMARY,
        CommonDataKinds.Phone.TYPE
    )
}