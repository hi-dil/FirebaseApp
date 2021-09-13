package com.haidil.udemyshops.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SoldProduct(
    val ownerID: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val soldQuantity: Int = 0,
    val image: String = "",
    val orderID: String = "",
    val orderDateTime: Long = 0L,
    val subTotalAmount: Double = 0.0,
    val shippingCharge: Double = 0.0,
    val totalAmount: Double = 0.0,
    val address: Address = Address(),
    var id: String = ""
): Parcelable