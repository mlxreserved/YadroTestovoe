package com.example.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class CustomAidlException (
    private val errorMessage: String?
) : Parcelable {


    fun toException(): Exception {
        return RuntimeException(errorMessage)
    }
}