package com.haidil.udemyshops.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.haidil.udemyshops.R
import com.haidil.udemyshops.model.Product
import com.haidil.udemyshops.ui.activities.ProductDetailsActivity
import com.haidil.udemyshops.ui.fragment.ProductFragment
import com.haidil.udemyshops.util.Constants
import com.haidil.udemyshops.util.GlideLoader
import com.haidil.udemyshops.util.MSPTextView
import com.haidil.udemyshops.util.MSPTextViewbold

open class MyProductListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>,
    private val fragment: ProductFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        val image = holder.itemView.findViewById<ImageView>(R.id.iv_item_image)

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, image)
            holder.itemView.findViewById<MSPTextViewbold>(R.id.tv_item_name).text =
                model.productTitle
            holder.itemView.findViewById<MSPTextView>(R.id.tv_item_price).text =
                String.format("RM%.2f", model.productPrice)

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).setOnClickListener {
                fragment.deleteProduct(model.id)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}