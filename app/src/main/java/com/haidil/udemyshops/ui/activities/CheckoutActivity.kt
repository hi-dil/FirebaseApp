package com.haidil.udemyshops.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.haidil.udemyshops.R
import com.haidil.udemyshops.databinding.ActivityCheckoutBinding
import com.haidil.udemyshops.firestore.FirestoreClass
import com.haidil.udemyshops.model.Address
import com.haidil.udemyshops.model.CartItem
import com.haidil.udemyshops.model.Order
import com.haidil.udemyshops.model.Product
import com.haidil.udemyshops.ui.adapters.ItemCartAdapter
import com.haidil.udemyshops.util.Constants

class CheckoutActivity : BaseActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private var mAddressDetails: Address? = null
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    private lateinit var mOrderDetails: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text =
                "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote
            binding.tvMobileNumber.text = resources.getString(
                R.string.bind_formatted_mobileNumber,
                mAddressDetails?.mobileNumber
            )

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
        }

        getProductList()

        binding.btnPlaceOrder.setOnClickListener { placeAnOrder() }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this)
    }

    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this)
    }

    private fun placeAnOrder() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mAddressDetails != null) {
            mOrderDetails = Order(
                FirestoreClass().getCurrentUserID(),
                mCartItemsList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubTotal,
                10.0,
                mTotalAmount,
                System.currentTimeMillis()
            )

            FirestoreClass().placeOrder(this, mOrderDetails)
        }
    }


    fun successProductListFromFireStore(productList: ArrayList<Product>) {
        mProductList = productList
        getCartItemsList()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductList) {
            for (cartItem in cartList) {
                if (product.id == cartItem.productID) {
                    cartItem.stockQuantity = product.productQuantity
                }
            }
        }

        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = ItemCartAdapter(this, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {
            val availableStock = item.stockQuantity
            if (availableStock > 0) {
                val price = item.price
                val quantity = item.cartQuantity
                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "RM$mSubTotal"
        binding.tvCheckoutShippingCharge.text = "RM10.00"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
            mTotalAmount = mSubTotal + 10.0
            binding.tvCheckoutTotalAmount.text = "RM$mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
    }

    fun orderPlacedSuccess() {
        FirestoreClass().updateAllDetails(this, mCartItemsList, mOrderDetails)
    }

    fun allDetailsUpdatedSuccessfully() {
        hideProgressDialog()
        Toast.makeText(this@CheckoutActivity, "Your order was place successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}