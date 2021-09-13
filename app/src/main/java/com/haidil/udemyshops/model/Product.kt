package com.haidil.udemyshops.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    var id: String = "",
    val userID: String = "",
    val userName: String = "",
    val productTitle: String = "",
    val productPrice: Double = 0.00,
    val productDescription: String = "",
    val productQuantity: Int = 0,
    val image: String = ""
): Parcelable