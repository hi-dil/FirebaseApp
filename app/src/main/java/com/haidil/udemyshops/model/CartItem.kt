package com.haidil.udemyshops.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val userID: String = "",
    val productOwnerID: String = "",
    val productID: String = "",
    val title: String = "",
    val price: Double = 0.00,
    val image: String = "",
    var cartQuantity: Int = 0,
    var stockQuantity: Int = 0,
    var id: String = ""
): Parcelable
