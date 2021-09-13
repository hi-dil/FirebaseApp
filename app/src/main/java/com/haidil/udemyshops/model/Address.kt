package com.haidil.udemyshops.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val userID: String = "",
    val name: String = "",
    val mobileNumber: Long = 0,
    val address: String = "",
    val zipCode: String = "",
    val additionalNote: String = "",
    val type: String = "",
    val otherDetails: String = "",
    var id: String = ""
): Parcelable
