package com.haidil.udemyshops.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.haidil.udemyshops.R
import com.haidil.udemyshops.firestore.FirestoreClass
import com.haidil.udemyshops.model.CartItem
import com.haidil.udemyshops.ui.activities.CartListActivity
import com.haidil.udemyshops.ui.activities.ProductDetailsActivity
import com.haidil.udemyshops.util.Constants
import com.haidil.udemyshops.util.GlideLoader
import com.haidil.udemyshops.util.MSPTextView
import com.haidil.udemyshops.util.MSPTextViewbold

open class ItemCartAdapter(
    private val context: Context,
    private val list: ArrayList<CartItem>,
    private val updateCartItem: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        val view = holder.itemView
        val image = view.findViewById<ImageView>(R.id.iv_cart_item_image)


        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, image)
            view.findViewById<MSPTextView>(R.id.tv_cart_item_title).text = model.title
            view.findViewById<MSPTextView>(R.id.tv_cart_quantity).text =
                model.cartQuantity.toString()
            view.findViewById<MSPTextViewbold>(R.id.tv_cart_item_price).text =
                String.format("RM%.2f", model.price)

            view.findViewById<ImageView>(R.id.iv_cart_item_image)
                .setOnClickListener { showProductDetails(model.productID) }

            view.findViewById<MSPTextView>(R.id.tv_cart_item_title)
                .setOnClickListener { showProductDetails(model.productID) }

            if (updateCartItem) {
                if (model.cartQuantity == 0) {
                    view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.GONE
                    view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.GONE

                    view.findViewById<TextView>(R.id.tv_cart_quantity).text =
                        context.resources.getString(R.string.lbl_out_of_stock)

                    view.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(
                        ContextCompat.getColor(context, R.color.snackBarError)
                    )

                } else {
                    view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                        View.VISIBLE
                    view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.VISIBLE

                    view.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(
                        ContextCompat.getColor(context, R.color.colorSecondaryText)
                    )
                }

                when {
                    model.cartQuantity <= 1 -> {
                        view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                            View.INVISIBLE
                    }
                    model.cartQuantity >= model.stockQuantity -> {
                        view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility =
                            View.INVISIBLE
                    }
                    else -> {
                        view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                            View.VISIBLE
                        view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility =
                            View.VISIBLE
                    }
                }

                view.findViewById<ImageButton>(R.id.ib_delete_cart_item).setOnClickListener {
                    when (context) {
                        is CartListActivity -> {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        }
                    }

                    FirestoreClass().removeItemFromCart(context, model.id)
                }

                view.findViewById<ImageButton>(R.id.ib_remove_cart_item).setOnClickListener {
                    if (model.cartQuantity <= 1) {
                        view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                            View.INVISIBLE

                        if (context is CartListActivity) {
                            context.showErrorSnackbar(
                                context.resources.getString(
                                    R.string.msg_for_available_stock,
                                    model.stockQuantity
                                ),
                                true
                            )
                        }
                    } else {
                        view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                            View.VISIBLE
                        val cartQuantity: Int = model.cartQuantity
                        val itemHashMap = HashMap<String, Any>()

                        itemHashMap[Constants.CART_QUANTITY] = cartQuantity - 1

                        if (context is CartListActivity) {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        }

                        FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                    }
                }

                view.findViewById<ImageButton>(R.id.ib_add_cart_item).setOnClickListener {
                    if (model.cartQuantity >= model.stockQuantity) {
                        view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility =
                            View.INVISIBLE
                    } else {
                        view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility =
                            View.VISIBLE
                        val cartQuantity: Int = model.cartQuantity
                        val itemHashMap = HashMap<String, Any>()

                        itemHashMap[Constants.CART_QUANTITY] = cartQuantity + 1

                        if (context is CartListActivity) {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        }

                        FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                    }
                }
            } else {
                view.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.GONE
                view.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.GONE
                view.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility = View.GONE
            }


        }
    }

    private fun showProductDetails(productID: String) {
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCT_ID, productID)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}