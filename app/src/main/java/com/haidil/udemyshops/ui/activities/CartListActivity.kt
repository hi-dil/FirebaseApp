package com.haidil.udemyshops.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivityCartListBinding
import com.haidil.udemyshops.firestore.FirestoreClass
import com.haidil.udemyshops.model.CartItem
import com.haidil.udemyshops.model.Product
import com.haidil.udemyshops.ui.adapters.ItemCartAdapter
import com.haidil.udemyshops.util.Constants

class CartListActivity : BaseActivity() {
    private lateinit var binding: ActivityCartListBinding
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartList: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductsList()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
        binding.rvCartItemsList.visibility = View.GONE
        binding.tvNoCartItemFound.visibility = View.VISIBLE
        binding.llCheckout.visibility = View.GONE

        for (product in mProductList) {
            for (cart in cartList) {
                if (product.id == cart.productID) {
                    cart.stockQuantity = product.productQuantity

                    if (product.productQuantity == 0) {
                        cart.cartQuantity = product.productQuantity
                    }
                }
            }
        }

        mCartList = cartList

        if (mCartList.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE
            binding.llCheckout.visibility = View.VISIBLE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val adapter = ItemCartAdapter(this@CartListActivity, mCartList, true)
            binding.rvCartItemsList.adapter = adapter

            var subTotal = 0.0
            for (item in mCartList) {
                val availableQuantity = item.stockQuantity

                if (availableQuantity > 0) {
                    subTotal += (item.price * item.cartQuantity)

                }
            }

            if (subTotal > 0) {
                binding.tvSubTotal.text = String.format("RM%.2f", subTotal)
                binding.tvShippingCharge.text = "RM10.00"
                binding.tvTotalAmount.text = String.format("RM%.2f", subTotal + 10)
            } else {
                binding.llCheckout.visibility = View.GONE
            }
        }
    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>) {
        //hideProgressDialog()
        mProductList = productList

        getCartItemsList()
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }

    fun itemUpdateSuccess() {
        hideProgressDialog()
        getCartItemsList()
    }

    private fun getProductsList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this)
    }

    private fun getCartItemsList() {
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.tbCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
        }

        binding.tbCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }
}