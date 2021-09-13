package com.haidil.udemyshops.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haidil.udemyshops.R
import com.haidil.udemyshops.model.Product
import com.haidil.udemyshops.util.GlideLoader
import com.haidil.udemyshops.util.MSPTextView
import com.haidil.udemyshops.util.MSPTextViewbold

open class DashboardItemsListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.findViewById(R.id.iv_dashboard_item_image)
            )

            holder.itemView.findViewById<MSPTextViewbold>(R.id.tv_dashboard_item_title).text =
                model.productTitle
            holder.itemView.findViewById<MSPTextView>(R.id.tv_dasbhoard_item_price).text =
                String.format("RM%.2f", model.productPrice)

            holder.itemView.setOnClickListener {
                if(onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener {
        fun onClick(position: Int, product: Product)
    }
}