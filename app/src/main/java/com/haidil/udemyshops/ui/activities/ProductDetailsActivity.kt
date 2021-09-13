package com.haidil.udemyshops.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivityProductDetailsBinding
import com.haidil.udemyshops.firestore.FirestoreClass
import com.haidil.udemyshops.model.CartItem
import com.haidil.udemyshops.model.Product
import com.haidil.udemyshops.util.Constants
import com.haidil.udemyshops.util.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductID: String = ""
    private lateinit var mProductDetails: Product
    private var mProductOwnerID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductID = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product ID", mProductID)
        }

        getProductDetails()
        binding.btnAddToCart.setOnClickListener(this)
        binding.btnGoToCart.setOnClickListener(this)
    }

    fun productDetailsSuccess(product: Product) {
        //var productOwnerID: String = ""
        mProductDetails = product

        GlideLoader(this).loadProductPicture(
            product.image,
            binding.ivProductDetailImage
        )

        binding.tvProductDetailsTitle.text = product.productTitle
        binding.tvProductDetailsPrice.text = String.format("RM%.2f", product.productPrice)
        binding.tvProductDetailsDescription.text = product.productDescription
        binding.tvProductDetailsAvailableQuantity.text = product.productQuantity.toString()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.GONE

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerID = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!

            if (FirestoreClass().getCurrentUserID() != mProductOwnerID) {
                binding.btnAddToCart.visibility = View.VISIBLE
            }
        }

        if (product.productQuantity == 0) {
            hideProgressDialog()

            binding.btnAddToCart.visibility = View.GONE

            binding.tvProductDetailsAvailableQuantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            binding.tvProductDetailsAvailableQuantity.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.snackBarError
                )
            )
        } else {
            if (FirestoreClass().getCurrentUserID() == product.userID) {
                hideProgressDialog()
            } else {
                FirestoreClass().isItemCartExist(this, mProductID)
            }
        }
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, mProductID)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductDetailsAcitivy)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
        }

        binding.toolbarProductDetailsAcitivy.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductOwnerID,
            mProductID,
            mProductDetails.productTitle,
            mProductDetails.productPrice,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun productExistsInCart() {
        hideProgressDialog()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_add_to_cart -> addToCart()

                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this, CartListActivity::class.java))

                }
            }
        }
    }
}