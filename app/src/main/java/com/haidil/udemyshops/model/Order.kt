package com.haidil.udemyshops.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val userID: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: Double = 0.0,
    val shipping_charge: Double = 0.0,
    val total_amount: Double = 0.0,
    val order_dateTime: Long = 0L,
    var id: String = ""
): Parcelable
